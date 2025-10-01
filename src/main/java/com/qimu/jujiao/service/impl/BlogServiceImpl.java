package com.qimu.jujiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.qimu.jujiao.common.ErrorCode;
import com.qimu.jujiao.exception.BusinessException;
import com.qimu.jujiao.model.entity.Blog;
import com.qimu.jujiao.model.entity.Comments;
import com.qimu.jujiao.model.entity.User;
import com.qimu.jujiao.model.request.BlogCreateRequest;
import com.qimu.jujiao.model.vo.BlogVo;
import com.qimu.jujiao.service.BlogService;
import com.qimu.jujiao.mapper.BlogMapper;
import com.qimu.jujiao.service.CommentsService;
import com.qimu.jujiao.service.UserService;
import com.qimu.jujiao.utils.LikeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
* @author 23776
* @description 针对表【blog】的数据库操作Service实现
* @createDate 2025-09-26 15:52:34
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService{

    @Resource
    private UserService userService;
    @Lazy
    @Resource
    private CommentsService commentsService;
    @Resource
    private LikeUtils likeUtils;

    @Override
    public void createBlog(BlogCreateRequest blogCreateRequest, User loginUser) {
        //对title字段进行验证
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogCreateRequest,blog);
        blog.setUserId(loginUser.getId());
        Gson gson = new Gson();
        blog.setImages(gson.toJson(blogCreateRequest.getImages()));
        blog.setLiked(0);
        blog.setComments(0);
        boolean save = this.save(blog);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public List<BlogVo> listBlogs(long currentPage, long pageSize, Long userId, User loginUser) {
        // 创建查询条件
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        // 如果指定了用户id，则只查询该用户的博客
        if (userId != null && userId > 0) {
            queryWrapper.eq(Blog::getUserId, userId);
        }
        // 按创建时间倒序排序
        queryWrapper.orderByDesc(Blog::getCreateTime);
        // 分页查询
        Page<Blog> page = new Page<>(currentPage, pageSize);
        Page<Blog> blogPage = this.page(page, queryWrapper);
        List<Blog> records = blogPage.getRecords();
        // 转换为BlogVo对象
        List<BlogVo> blogVoList = new ArrayList<>();
        for (Blog blog : records) {
            BlogVo blogVo = new BlogVo();
            BeanUtils.copyProperties(blog, blogVo);
            // 查询创建用户信息
            User user = userService.getById(blog.getUserId());
            if (user != null) {
                // 脱敏后设置用户信息
                blogVo.setUser(userService.getSafetyUser(user));
            }
            // 设置当前用户是否点赞
            if (loginUser != null) {
                blogVo.setIsLiked(likeUtils.isBlogLiked(blog.getId(), loginUser.getId()));
            } else {
                blogVo.setIsLiked(false);
            }
            blogVoList.add(blogVo);
        }
        return blogVoList;
    }

    @Override
    public BlogVo getBlogById(Long id, User loginUser) {
        // 1. 校验博客是否存在
        Blog blog = this.getById(id);
        if (blog == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "博客不存在");
        }

        // 2. 转换为 BlogVo
        BlogVo blogVo = new BlogVo();
        BeanUtils.copyProperties(blog, blogVo);

        // 3. 关联用户信息
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        if (user != null) {
            blogVo.setUser(userService.getSafetyUser(user));
        }

        // 4. 获取点赞状态和点赞数
        if (loginUser != null) {
            blogVo.setIsLiked(likeUtils.isBlogLiked(id, loginUser.getId()));
        }
        return blogVo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likeBlog(Long blogId, User loginUser) {
        // 1. 校验博客是否存在
        Blog blog = this.getById(blogId);
        if (blog == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "博客不存在");
        }

        // 2. 更新点赞状态和点赞数
        int delta = likeUtils.likeBlog(blogId, loginUser.getId());
        blog.setLiked(blog.getLiked() + delta);
        return this.updateById(blog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBlog(Long blogId, User loginUser) {
        // 1. 校验博客是否存在
        Blog blog = this.getById(blogId);
        if (blog == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "博客不存在");
        }

        // 2. 校验是否有权限删除
        if (!blog.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        // 3. 删除博客相关的评论
        LambdaQueryWrapper<Comments> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.eq(Comments::getBlogId, blogId);
        commentsService.remove(commentWrapper);

        // 4. 删除博客相关的点赞记录
        likeUtils.deleteLikeBlog(blogId);
        // 5. 删除博客
        boolean success = this.removeById(blogId);
        if (!success) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "博客删除失败");
        }
    }
}




