package com.qimu.jujiao.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 点赞工具类
 */
@Component
public class LikeUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String BLOG_LIKE_KEY = "blog:like:";
    private static final String COMMENT_LIKE_KEY = "comment:like:";

    /**
     * 判断用户是否点赞了博客
     *
     * @param blogId 博客id
     * @param userId 用户id
     * @return 是否点赞
     */
    public boolean isBlogLiked(Long blogId, Long userId) {
        String key = BLOG_LIKE_KEY + blogId;
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, userId.toString()));
    }

    /**
     * 博客点赞
     *
     * @param blogId 博客id
     * @param userId 用户id
     * @return 点赞数变化（1表示点赞，-1表示取消点赞）
     */
    public int likeBlog(Long blogId, Long userId) {
        String key = BLOG_LIKE_KEY + blogId;
        boolean isMember = Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, userId.toString()));
        if (isMember) {
            // 取消点赞
            stringRedisTemplate.opsForSet().remove(key, userId.toString());
            return -1;
        } else {
            // 点赞
            stringRedisTemplate.opsForSet().add(key, userId.toString());
            return 1;
        }
    }

    /**
     * 获取博客点赞数
     *
     * @param blogId 博客id
     * @return 点赞数
     */
    public long getBlogLikeCount(Long blogId) {
        String key = BLOG_LIKE_KEY + blogId;
        Long size = stringRedisTemplate.opsForSet().size(key);
        return size == null ? 0 : size;
    }

    /**
     * 判断用户是否点赞了评论
     *
     * @param commentId 评论id
     * @param userId 用户id
     * @return 是否点赞
     */
    public boolean isCommentLiked(Long commentId, Long userId) {
        String key = COMMENT_LIKE_KEY + commentId;
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, userId.toString()));
    }

    /**
     * 评论点赞
     *
     * @param commentId 评论id
     * @param userId 用户id
     * @return 点赞数变化（1表示点赞，-1表示取消点赞）
     */
    public int likeComment(Long commentId, Long userId) {
        String key = COMMENT_LIKE_KEY + commentId;
        boolean isMember = Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(key, userId.toString()));
        if (isMember) {
            // 取消点赞
            stringRedisTemplate.opsForSet().remove(key, userId.toString());
            return -1;
        } else {
            // 点赞
            stringRedisTemplate.opsForSet().add(key, userId.toString());
            return 1;
        }
    }

    /**
     * 获取评论点赞数
     *
     * @param commentId 评论id
     * @return 点赞数
     */
    public long getCommentLikeCount(Long commentId) {
        String key = COMMENT_LIKE_KEY + commentId;
        Long size = stringRedisTemplate.opsForSet().size(key);
        return size == null ? 0 : size;
    }

    /**
     * 删除博客的点赞记录
     *
     * @param blogId 博客id
     */
    public void deleteLikeBlog(Long blogId) {
        String key = BLOG_LIKE_KEY + blogId;
        stringRedisTemplate.delete(key);
    }

    /**
     * 删除评论的点赞记录
     *
     * @param commentId 评论id
     */
    public void deleteLikeComment(Long commentId) {
        String key = COMMENT_LIKE_KEY + commentId;
        stringRedisTemplate.delete(key);
    }
}
