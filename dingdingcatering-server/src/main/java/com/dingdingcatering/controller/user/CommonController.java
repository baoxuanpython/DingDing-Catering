package com.dingdingcatering.controller.user;

import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.common.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * C端公共控制器
 */
@Slf4j
@RestController
@RequestMapping("/user/common")
public class CommonController {

    private final FileUploadService fileUploadService;

    public CommonController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * C端文件上传接口（阿里云OSS）。
     * <p>
     * 与管理端 /admin/common/upload 的区别：本接口走用户端 JWT 校验，
     * 小程序只持有用户端令牌（请求头 authentication），无法通过管理端拦截器。
     *
     * @param file 待上传的文件
     * @return 文件访问URL
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        return Result.success(fileUploadService.upload(file));
    }
}