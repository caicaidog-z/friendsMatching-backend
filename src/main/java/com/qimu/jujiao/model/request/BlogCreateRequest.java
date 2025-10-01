package com.qimu.jujiao.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BlogCreateRequest implements Serializable {


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 相关照片，最多9张，多张以","隔开
     */
    private List<String> images;

    /**
     * 博客的内容
     */
    private String content;

}
