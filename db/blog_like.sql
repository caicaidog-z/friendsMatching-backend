-- auto-generated definition
create table blog_like
(
    id         bigint auto_increment comment '点赞记录id'
        primary key,
    userId     bigint                             not null comment '点赞用户id',
    blogId     bigint                             not null comment '被点赞博客id',
    status     tinyint  default 1                 not null comment '点赞状态（1-已点赞 0-已取消）',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    constraint unique_user_blog
        unique (userId, blogId)
)
    comment '博客点赞表' charset = utf8mb4;