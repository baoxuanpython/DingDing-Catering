package com.sky.controller;

import com.sky.exception.UploadFileFailException;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping("/admin/common")
public class UploadController {
    private final AliOssUtil aliOssUtil;

    public UploadController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<String> upload(MultipartFile file) {
        // 上传文件到OSS
        try {
            log.info("上传文件，文件名：{}", file.getOriginalFilename());
            String fileName = aliOssUtil.upload(file.getBytes(), file.getOriginalFilename());
            return Result.success(fileName);
        } catch (Exception e) {
            throw new UploadFileFailException(e.getMessage());
        }
    }
}
