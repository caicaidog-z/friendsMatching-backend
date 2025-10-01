package com.qimu.jujiao.model.vo;

import com.qimu.jujiao.model.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CommentVo implements Serializable {
    /**
     * 评论id
     */
    private Long id;

    /**
     * 评论用户信息
     */
    private User user;

    /**
     * 博客id
     */
    private Long blogId;

    /**
     * 父评论id
     */
    private Long parentId;

    /**
     * 根评论id
     */
    private Long answerId;

//    /**
//     * 回复的用户信息
//     */
//    private User answerUser;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer liked;

    /**
     * 当前用户是否点赞
     */
    private Boolean isLiked;

    /**
     * 评论状态（0-正常）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 子评论列表
     */
    private List<CommentVo> children;
}