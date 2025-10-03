# 中工交友平台

> 作者：菜菜狗
>
> [Github 主页](https://github.com/caicaidog-z)





## 项目介绍

中工交友平台是一个移动端网页的在线云交友平台。实现了按标签匹配、查找用户，基于 Redis GEO 实现搜索附近用户，同时个人还可以建队、组队以打造个人学习队伍。除了添加好友、搜索好友外，还基于 Websocket 实现好友间私聊，方便用户寻找志同道合的学习搭子。

### 线上地址

[中工交友平台](http://friend.caicaidog.xyz)



## 技术选型

### 前端

| 技术       | 用途                       | 版本   |
| ---------- | -------------------------- | ------ |
| Vue        | 前端经典框架，方便开发页面 | 3.3.11 |
| Vue-Router | 细致的导航控制             | 4      |
| Axios      | 发送请求至后端             | 1.6.2  |
| Vant       | 移动端样式组件库           | 4.8.0  |
| Vite       | 前端构建工具               | 5.0.8  |



### 后端

| 技术                 | 用途                                         | 版本    |
| -------------------- | -------------------------------------------- | ------- |
| Spring Boot          | 快构建 Spring 应用                           | 2.7.6   |
| JDK                  | Java 应用开发工具                            | 1.8     |
| MyBatis              | 操作数据库的框架                             | 3.5.2   |
| MyBatis-Plus         | MyBatis的增强框架，无需编写 SQL 语句         | 3.5.2   |
| MySQL                | 一个关系型数据库产品，用于存储数据           | 8.0..33 |
| Redis                | 一个非关系型数据库产品，用于存储数据         | 5.      |
| WebSocket            | 使得客户端和服务器之间的数据交换变得更加简单 | 2.4.1   |
| Lombok               | 实体类方法的快速生成工具                     |         |
| knife4j              | 在线接口文档生成的库                         | 2.0.9   |
| EasyExcel            | 快速、低占用地操作 Excel                     | 3.3.2   |
| hutool               | 强而全的工具库                               | 5.7.17  |
| Guava-Retrying       | 提供重试机制的库                             | 1.0.6   |
| Apache-commons-lang3 | 工具库                                       | 3.12.0  |





> 觉得项目还不错的同学可否给我项目点个 Star 呢？如果可以，小弟不胜感激，谢谢！

## 功能展示

登录

<img src="C:\Users\23776\AppData\Roaming\Typora\typora-user-images\image-20251003175053926.png" alt="image-20251003175053926" style="zoom:33%;" />

注册

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609142952.png" alt="image-20240609142952622" style="zoom: 67%;" />

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609143117.png" alt="image-20240609143117032" style="zoom: 67%;" />

首页

<img src="C:\Users\23776\AppData\Roaming\Typora\typora-user-images\image-20251003175152314.png" alt="image-20251003175152314" style="zoom:33%;" />

按标签匹配相似用户

<img src="C:\Users\23776\AppData\Roaming\Typora\typora-user-images\image-20251003175831139.png" alt="image-20251003175831139" style="zoom:33%;" />

按标签搜索用户

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609145012.png" alt="image-20240609145012357" style="zoom:67%;" />

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609145002.png" alt="image-20240609145002577" style="zoom:67%;" />

按距离搜索用户

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609145122.png" alt="image-20240609145122634" style="zoom:67%;" />

好友页面

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609145434.png" alt="image-20240609145434695" style="zoom:67%;" />

搜索好友

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609171231.png" alt="image-20240609171231079" style="zoom:67%;" />

好友私聊

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609145534.png" alt="image-20240609145534153" style="zoom:67%;" />

ai对话

<img src="C:\Users\23776\AppData\Roaming\Typora\typora-user-images\image-20251003174245967.png" alt="image-20251003174245967" style="zoom: 33%;" />

建队

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609145549.png" alt="image-20240609145549455" style="zoom:67%;" />

个人页面

<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609143327.png" alt="image-20240609143327588" style="zoom: 67%;" />



<img src="https://hejiajun-img-bucket.oss-cn-wuhan-lr.aliyuncs.com/img/20240609143357.png" alt="image-20240609143357705" style="zoom:67%;" />



## 项目亮点

1. 基于 Spring AOP + Axios 实现前端登录拦截
2. 基于 Redis 实现分布式 Session 存储
3. 使用 Redis List 结构配合 Vue-infinite-loading 组件实现滑动加载
4. Spring Scheduling + Redis 分布式锁实现缓存预热
5. 了解编辑距离算法，可用于匹配相似字符串，单词校验
6. 基于 Redis GEO 存储用户地理微信信息，实现搜索附近用户
7. 基于 Redis 分布式锁防止用户重复入队
8. 通过 Guava 库实现重试机制，保证缓存数据一致性
9. 基于 Websocket 实现用户间私聊
10. 集成第三方库生成接口测试文档，方便测试项目接口
11. 熟悉 EasyExcel 的使用
12. 基于 Axios 封装请求实例，方便请求后端接口
13. 熟悉 Vant 组件库的使用
14. 熟悉 Vue3 setup 语法
15. 掌握 Vue-Router 基本使用



## 数据库表

> 如果大家拉取了后端源码，直接找到 sql/create_sql.sql 文件直接运行即可创建相应数据库和表结构

### 用户表

```sql
/*用户表*/
create table hjj.user
(
    id           bigint auto_increment comment 'id'
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账户',
    avatarUrl    varchar(1024)                      null comment '用户头像' default 'https://www.keaitupian.cn/cjpic/frombd/0/253/936677050/470164789.jpg',
    gender       tinyint                            null comment '用户性别',
    profile      varchar(512)                       null comment '个人简介',
    userPassword varchar(512)                       not null comment '用户密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '状态 0 - 正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    planetCode   varchar(512)                       null comment '星球编号',
    tags         varchar(1024)                      null comment '标签列表(json)',
    longitude    decimal(10, 6)                     null comment '经度',
    dimension    decimal(10, 6)                     null comment '纬度'
)
    comment '用户';
```



### 队伍表

```sql
/*队伍表*/
create table if not exists hjj.team
(
    id           bigint auto_increment comment 'id'
        primary key,
    teamName   		varchar(256)                        not null comment '队伍名称',
    description 	varchar(1024)                       null comment ' 描述',
    maxNum        	int    default 1              		null comment '最大人数',
    expireTime      datetime							null comment '过期时间',
    userId 			bigint                              not null comment '队伍创建者/队长id',
    status         	tinyint default 0 		        	null comment '队伍状态 - 0 - 公开， 1 - 私有，2 - 加密
- ',
    password        varchar(512)                       null comment '队伍密码',
    createTime   	datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   	datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     	tinyint  default 0                 not null comment '是否删除'
)
    comment '队伍信息';
```



### 用户关系表

```sql
/*用户队伍关系*/
create table if not exists hjj.user_team
(
    id           bigint auto_increment comment 'id'
        primary key,
    userId 			bigint                             	comment '用户id',
    teamId 			bigint                             	comment '队伍id',
    joinTime   	datetime 							    comment '加入时间',
    createTime   	datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   	datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     	tinyint  default 0                 not null comment '是否删除'
)
    comment '用户队伍关系表';
```



### 好友表

```sql
/*好友表*/
create table hjj.friend
(
    id         bigint auto_increment comment 'id'
        primary key,
    userId     bigint                             not null comment '用户id（即自己id）',
    friendId   bigint                             not null comment '好友id',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '好友表';
```



### 聊天表

```sql
/*聊天表*/
CREATE TABLE `chat`  (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '聊天记录id',
                         `fromId` bigint(20) NOT NULL COMMENT '发送消息id',
                         `toId` bigint(20) NULL DEFAULT NULL COMMENT '接收消息id',
                         `text` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
                         `chatType` tinyint(4) NOT NULL COMMENT '聊天类型 1-私聊 2-群聊',
                         `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
                         `teamId` bigint(20) NULL DEFAULT NULL,
                         `isDelete` tinyint(4) NULL DEFAULT 0,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天消息表' ROW_FORMAT = COMPACT;
```



