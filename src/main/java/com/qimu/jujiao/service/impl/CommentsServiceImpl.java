package com.qimu.jujiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qimu.jujiao.common.ErrorCode;
import com.qimu.jujiao.exception.BusinessException;
import com.qimu.jujiao.model.entity.Blog;
import com.qimu.jujiao.model.entity.Comments;
import com.qimu.jujiao.model.entity.User;
import com.qimu.jujiao.model.request.CommentCreateRequest;
import com.qimu.jujiao.model.vo.CommentVo;
import com.qimu.jujiao.service.BlogService;
import com.qimu.jujiao.service.CommentsService;
import com.qimu.jujiao.mapper.CommentsMapper;
import com.qimu.jujiao.service.UserService;
import com.qimu.jujiao.utils.CommentVosUtils;
import com.qimu.jujiao.utils.LikeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 23776
* @description 针对表【comments】的数据库操作Service实现
* @createDate 2025-09-26 15:53:42
*/
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
    implements CommentsService{

    @Resource
    private BlogService blogService;

    @Resource
    private UserService userService;

    @Resource
    private LikeUtils likeUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createComment(CommentCreateRequest commentCreateRequest, User loginUser) {
        // 1. 校验博客是否存在
        Long blogId = commentCreateRequest.getBlogId();
        Blog blog = blogService.getById(blogId);
        if (blog == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "博客不存在");
        }

        // 2. 如果是回复评论，校验父评论和回复的评论是否存在
        Long parentId = commentCreateRequest.getParentId();
        Long answerId = commentCreateRequest.getAnswerId();
        if (parentId != null) {
            Comments parentComment = this.getById(parentId);
            if (parentComment == null) {
                throw new BusinessException(ErrorCode.NULL_ERROR, "父评论不存在");
            }
            if (answerId != null) {
                Comments answerComment = this.getById(answerId);
                if (answerComment == null) {
                    throw new BusinessException(ErrorCode.NULL_ERROR, "根评论不存在");
                }
            }
        }

        // 3. 创建评论
        Comments comment = new Comments();
        comment.setBlogId(blogId);
        comment.setUserId(loginUser.getId());
        if(parentId == null){
            comment.setParentId(0L);
        }else {
            comment.setParentId(parentId);
        }
        if(answerId == null){
            comment.setAnswerId(0L);
        }else {
            comment.setAnswerId(answerId);
        }
        comment.setContent(commentCreateRequest.getContent());
        comment.setLiked(0);
        comment.setStatus(0);
        boolean success = this.save(comment);
        if (!success) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论创建失败");
        }

        // 4. 更新博客评论数
        blog.setComments(blog.getComments() + 1);
        blogService.updateById(blog);
    }

    @Override
    public List<CommentVo> listCommentsByBlogId(Long blogId, User loginUser) {
        // 1. 查询所有评论
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getBlogId, blogId);
        queryWrapper.eq(Comments::getStatus, 0);
        queryWrapper.orderByDesc(Comments::getCreateTime);
        List<Comments> commentsList = this.list(queryWrapper);

        // 2. 获取所有评论用户id
        List<Long> userIds = commentsList.stream()
                .map(Comments::getUserId)
                .distinct()
                .collect(Collectors.toList());

        // 3. 查询所有用户信息
        Map<Long, User> userMap = new HashMap<>();
        for (Long userId : userIds) {
            User user = userService.getById(userId);
            if (user != null) {
                userMap.put(userId, userService.getSafetyUser(user));
            }
        }

        // 4. 转换为 CommentVo 并设置用户信息
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comments comment : commentsList) {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);

            // 设置评论用户信息
            commentVo.setUser(userMap.get(comment.getUserId()));

//            // 设置回复用户信息
//            if (comment.getAnswerId() != null && comment.getAnswerId() > 0) {
//                Comments answerComment = this.getById(comment.getAnswerId());
//                if (answerComment != null) {
//                    commentVo.setAnswerUser(userMap.get(answerComment.getUserId()));
//                }
//            }

            // 设置点赞状态和点赞数
            if (loginUser != null) {
                commentVo.setIsLiked(likeUtils.isCommentLiked(comment.getId(), loginUser.getId()));
            }

            commentVoList.add(commentVo);
        }

        // 5. 使用工具类构建评论树
        return CommentVosUtils.processCommentVos(commentVoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likeComment(Long commentId, User loginUser) {
        // 1. 校验评论是否存在
        Comments comment = this.getById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "评论不存在");
        }

        // 2. 更新点赞状态和点赞数
        int delta = likeUtils.likeComment(commentId, loginUser.getId());
        comment.setLiked(comment.getLiked() + delta);
        return this.updateById(comment);
    }

    @Override
    public void deleteComment(Long commentId, User loginUser) {
        // 1. 校验评论是否存在
        Comments comment = this.getById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "评论不存在");
        }

        // 2. 校验是否有权限删除
        if (!comment.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        // 3. 删除评论
        boolean success = this.removeById(commentId);
        if (!success) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论删除失败");
        }

        // 4. 更新博客评论数
        Blog blog = blogService.getById(comment.getBlogId());
        if (blog != null) {
            blog.setComments(blog.getComments() - 1);
            blogService.updateById(blog);
        }
    }

    /**
     * 判断用户是否点赞了评论
     * @param commentId 评论id
     * @param userId 用户id
     * @return 是否点赞
     */
    private boolean isCommentLiked(Long commentId, Long userId) {
        String key = String.format("comment:like:%d", commentId);
        return false; // TODO: 实现评论点赞功能
    }

    /**
     * 添加评论点赞记录
     * @param commentId 评论id
     * @param userId 用户id
     */
    private void addCommentLike(Long commentId, Long userId) {
        String key = String.format("comment:like:%d", commentId);
        // TODO: 实现评论点赞功能
    }

    /**
     * 移除评论点赞记录
     * @param commentId 评论id
     * @param userId 用户id
     */
    private void removeCommentLike(Long commentId, Long userId) {
        String key = String.format("comment:like:%d", commentId);
        // TODO: 实现评论点赞功能
    }
}




