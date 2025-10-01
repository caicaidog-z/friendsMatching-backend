package com.qimu.jujiao.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qimu.jujiao.model.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BlogVo implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 创建用户的相关信息
     */
    private User user;

    /**
     * 标题
     */
    private String title;

    /**
     * 相关照片，最多9张，多张以","隔开
     */
    private String images;

    /**
     * 博客的内容
     */
    private String content;

    /**
     * 点赞数量
     */
    private Integer liked;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 当前用户是否点赞
     */
    private Boolean isLiked;
}
