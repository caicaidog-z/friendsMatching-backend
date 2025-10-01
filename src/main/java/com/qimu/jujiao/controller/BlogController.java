package com.qimu.jujiao.controller;

import com.qimu.jujiao.common.BaseResponse;
import com.qimu.jujiao.common.ErrorCode;
import com.qimu.jujiao.common.ResultUtil;
import com.qimu.jujiao.exception.BusinessException;
import com.qimu.jujiao.model.entity.User;
import com.qimu.jujiao.model.request.BlogCreateRequest;
import com.qimu.jujiao.model.vo.BlogVo;
import com.qimu.jujiao.service.BlogService;
import com.qimu.jujiao.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author caicaidog
 * @Description
 * @Data 下午4:00 2025/9/26
 **/
@RestController
@RequestMapping("blog")
@Api(tags = "博客相关接口")
public class BlogController {
    @Resource
    private UserService userService;
    @Resource
    private BlogService blogService;

    /**
     * 创建博客
     * @param blogCreateRequest 博客创建请求
     * @param request 请求对象
     * @return 创建结果
     */
    @PostMapping("create")
    @ApiOperation("创建博客")
    public BaseResponse<String> createBlog(@RequestBody BlogCreateRequest blogCreateRequest, HttpServletRequest request) {
        userService.isLogin(request);
        if (blogCreateRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        blogService.createBlog(blogCreateRequest, loginUser);
        return ResultUtil.success("添加成功");
    }

    /**
     * 获取博客列表
     * @param currentPage 当前页码
     * @param pageSize 每页大小
     * @param userId 用户id（可选）
     * @param request 请求对象
     * @return 博客列表
     */
    @GetMapping("list")
    @ApiOperation("获取博客列表")
    public BaseResponse<List<BlogVo>> listBlogs(
            @RequestParam(defaultValue = "1") long currentPage,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) Long userId,
            HttpServletRequest request) {
        if (currentPage <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分页参数错误");
        }
        User loginUser = userService.getLoginUser(request);
        List<BlogVo> blogVoList = blogService.listBlogs(currentPage, pageSize, userId, loginUser);
        return ResultUtil.success(blogVoList);
    }

    /**
     * 获取博客详情
     * @param id 博客id
     * @param request 请求对象
     * @return 博客详情
     */
    @GetMapping("{id}")
    @ApiOperation("获取博客详情")
    public BaseResponse<BlogVo> getBlogById(@PathVariable("id") Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        BlogVo blogVo = blogService.getBlogById(id, loginUser);
        return ResultUtil.success(blogVo);
    }

    /**
     * 点赞或取消点赞博客
     * @param id 博客id
     * @param request 请求对象
     * @return 点赞结果
     */
    @PostMapping("{id}/like")
    @ApiOperation("点赞或取消点赞博客")
    public BaseResponse<Boolean> likeBlog(@PathVariable("id") Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        return ResultUtil.success(blogService.likeBlog(id,loginUser));
    }



    /**
     * 删除博客
     *
     * @param id 博客id
     * @param request HTTP请求
     * @return 通用返回
     */
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> deleteBlog(@PathVariable("id") Long id,
                                         HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        blogService.deleteBlog(id, loginUser);
        return ResultUtil.success(true);
    }
}
