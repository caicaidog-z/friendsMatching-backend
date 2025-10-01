package com.qimu.jujiao.service;

import com.qimu.jujiao.model.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qimu.jujiao.model.entity.User;
import com.qimu.jujiao.model.request.BlogCreateRequest;
import com.qimu.jujiao.model.vo.BlogVo;

import java.util.List;

/**
* @author 23776
* @description 针对表【blog】的数据库操作Service
* @createDate 2025-09-26 15:52:34
*/
public interface BlogService extends IService<Blog> {

    void createBlog(BlogCreateRequest blogCreateRequest, User loginUser);

    /**
     * 获取博客列表
     * @param currentPage 当前页码
     * @param pageSize 每页大小
     * @param userId 用户id，可选，用于获取指定用户的博客
     * @param loginUser 当前登录用户，用于判断是否点赞
     * @return 博客列表
     */
    List<BlogVo> listBlogs(long currentPage, long pageSize, Long userId, User loginUser);

    /**
     * 获取博客详情
     * @param id 博客id
     * @param loginUser 当前登录用户，用于判断是否点赞
     * @return 博客详情
     */
    BlogVo getBlogById(Long id, User loginUser);

    boolean likeBlog(Long blogId, User loginUser);

    /**
     * 删除博客
     *
     * @param blogId 博客id
     * @param loginUser 当前登录用户
     */
    void deleteBlog(Long blogId, User loginUser);
}
