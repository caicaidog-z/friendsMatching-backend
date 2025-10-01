package com.qimu.jujiao.service;

import com.qimu.jujiao.model.entity.Comments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qimu.jujiao.model.entity.User;
import com.qimu.jujiao.model.request.CommentCreateRequest;
import com.qimu.jujiao.model.vo.CommentVo;

import java.util.List;

/**
* @author 23776
* @description 针对表【comments】的数据库操作Service
* @createDate 2025-09-26 15:53:42
*/
public interface CommentsService extends IService<Comments> {
    /**
     * 创建评论
     * @param commentCreateRequest 评论创建请求
     * @param loginUser 当前登录用户
     */
    void createComment(CommentCreateRequest commentCreateRequest, User loginUser);

    /**
     * 获取博客评论列表
     * @param blogId 博客id
     * @param loginUser 当前登录用户
     * @return 评论列表
     */
    List<CommentVo> listCommentsByBlogId(Long blogId, User loginUser);

    /**
     * 点赞评论
     * @param commentId 评论id
     * @param loginUser 当前登录用户
     * @return 是否点赞成功
     */
    boolean likeComment(Long commentId, User loginUser);

    /**
     * 删除评论
     * @param commentId 评论id
     * @param loginUser 当前登录用户
     */
    void deleteComment(Long commentId, User loginUser);
}
