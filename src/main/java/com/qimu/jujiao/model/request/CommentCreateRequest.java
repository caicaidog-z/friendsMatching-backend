package com.qimu.jujiao.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentCreateRequest implements Serializable {
    /**
     * 博客id
     */
    private Long blogId;

    /**
     * 父评论id，如果是一级评论，则为null
     */
    private Long parentId;

    /**
     * 回复的评论id，如果不是回复评论，则为null
     */
    private Long answerId;

    /**
     * 评论内容
     */
    private String content;
}