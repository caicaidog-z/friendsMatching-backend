package com.qimu.jujiao.controller;

import com.qimu.jujiao.common.BaseResponse;
import com.qimu.jujiao.common.ErrorCode;
import com.qimu.jujiao.common.ResultUtil;
import com.qimu.jujiao.exception.BusinessException;
import com.qimu.jujiao.model.entity.User;
import com.qimu.jujiao.model.request.CommentCreateRequest;
import com.qimu.jujiao.model.vo.CommentVo;
import com.qimu.jujiao.service.CommentsService;
import com.qimu.jujiao.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @Author caicaidog
 * @Description  
 * @Data 下午4:38 2025/9/26
 **/
@RestController
@RequestMapping("comments")
public class CommentsController {

    @Resource
    private UserService userService;
    @Resource
    private CommentsService commentsService;
    /**
     * 创建评论
     *
     * @param commentCreateRequest 评论创建请求
     * @param request HTTP请求
     * @return 通用返回
     */
    @PostMapping("/comment")
    public BaseResponse<Boolean> createComment(@RequestBody CommentCreateRequest commentCreateRequest,
                                               HttpServletRequest request) {
        if (commentCreateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        commentsService.createComment(commentCreateRequest, loginUser);
        return ResultUtil.success(true);
    }

    /**
     * 获取博客评论列表
     *
     * @param blogId 博客id
     * @param request HTTP请求
     * @return 评论列表
     */
    @GetMapping("/comment/list")
    public BaseResponse<List<CommentVo>> listComments(@RequestParam("blogId") Long blogId,
                                                      HttpServletRequest request) {
        if (blogId == null || blogId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        List<CommentVo> commentVoList = commentsService.listCommentsByBlogId(blogId, loginUser);
        return ResultUtil.success(commentVoList);
    }

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     * @param request HTTP请求
     * @return 通用返回
     */
    @PostMapping("/comment/{id}/like")
    public BaseResponse<Boolean> likeComment(@PathVariable("id") Long commentId,
                                             HttpServletRequest request) {
        if (commentId == null || commentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = commentsService.likeComment(commentId, loginUser);
        return ResultUtil.success(result);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     * @param request HTTP请求
     * @return 通用返回
     */
    @DeleteMapping("/comment/{id}")
    public BaseResponse<Boolean> deleteComment(@PathVariable("id") Long commentId,
                                               HttpServletRequest request) {
        if (commentId == null || commentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        commentsService.deleteComment(commentId, loginUser);
        return ResultUtil.success(true);
    }
}
