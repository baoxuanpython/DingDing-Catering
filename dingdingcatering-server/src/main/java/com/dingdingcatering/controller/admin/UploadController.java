package com.dingdingcatering.controller.admin;

import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.common.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class UploadController {

    private final FileUploadService fileUploadService;

    public UploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * 文件上传接口（阿里云OSS）。
     * <p>
     * 处理流程：
     * 1. 接收前端上传的文件
     * 2. 校验文件（非空、大小、图片格式）
     * 3. 调用阿里云OSS SDK上传文件
     * 4. 返回文件访问URL
     *
     * @param file 待上传的文件
     * @return 文件访问URL
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        return Result.success(fileUploadService.upload(file));
    }
}