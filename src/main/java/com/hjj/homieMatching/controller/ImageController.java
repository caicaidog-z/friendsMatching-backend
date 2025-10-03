package com.hjj.homieMatching.controller;

import cn.hutool.core.io.FileUtil;
import com.hjj.homieMatching.common.BaseResponse;
import com.hjj.homieMatching.common.ErrorCode;
import com.hjj.homieMatching.common.ResultUtils;
import com.hjj.homieMatching.config.CosClientConfig;
import com.hjj.homieMatching.constant.RedisConstant;
import com.hjj.homieMatching.exception.BusinessException;
import com.hjj.homieMatching.manager.AliOSSManager;
import com.hjj.homieMatching.manager.CosManager;
import com.hjj.homieMatching.manager.RedisLimiterManager;
import com.hjj.homieMatching.model.domain.User;
import com.hjj.homieMatching.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Resource
    private AliOSSManager aliOSSManager;
    @Resource
    private CosManager cosManager;
    @Resource
    private RedisLimiterManager redisLimiterManager;
    @Resource
    private CosClientConfig cosClientConfig;
    @Resource
    private UserService userService;

    @PostMapping("/blog/coverImage/upload")
    public BaseResponse<String> uploadBlogCoverImage(@RequestPart(value = "file") MultipartFile multipartFile,
                                                     HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();
        String url = null;
        // 限制每个用户每分钟上传 1 次
        redisLimiterManager.doRateLimiter(RedisConstant.BLOG_COVER_IMAGE_UPLOAD_KEY + userId, 1, 4);
        validFile(multipartFile);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", "blog_coverImage", loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success(cosClientConfig.getHost() + '/' + filepath);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
            }
        }
//        try {
//            url = aliOSSManager.upload(file);
//        } catch (IOException e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传图片失败");
//        }
//        return ResultUtils.success(url);

    }

    @PostMapping("/blog/image/upload")
    public BaseResponse<String> uploadBlogImage(@RequestPart(value = "file") MultipartFile multipartFile, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();
        String url = null;
        // 限制每个用户每分钟上传 2 次
        redisLimiterManager.doRateLimiter(RedisConstant.BLOG_IMAGE_UPLOAD_KEY + userId, 1, 10);
//        try {
//            url = aliOSSManager.upload(multipartFile);
//        } catch (IOException e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传图片失败");
//        }
//        return ResultUtils.success(url);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", "blog_image", loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success(cosClientConfig.getHost() + '/' + filepath);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
            }
        }
    }

    @PostMapping("/avatar/upload")
    public BaseResponse<String> uploadAvatar(@RequestPart(value = "file") MultipartFile multipartFile, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();
        String url = null;
        // 限制每个用户每分钟上传 2 次
        redisLimiterManager.doRateLimiter(RedisConstant.BLOG_IMAGE_UPLOAD_KEY + userId, 1, 4);
//        try {
//            url = aliOSSManager.upload(file);
//        } catch (IOException e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传图片失败");
//        }
//        return ResultUtils.success(url);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", "avatar", loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success(cosClientConfig.getHost() + '/' + filepath);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
            }
        }
    }
    /**
     * 校验文件
     *
     * @param multipartFile
     */
    private void validFile(MultipartFile multipartFile) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
            if (fileSize > 1024 * 1024L) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
    }
}
