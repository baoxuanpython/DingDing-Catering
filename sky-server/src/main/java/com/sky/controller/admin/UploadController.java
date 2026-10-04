package com.sky.controller.admin;

import com.sky.annotation.AutoLogDTO;
import com.sky.constant.MessageConstant;
import com.sky.exception.UploadFileFailException;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
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
    @AutoLogDTO("上传文件")
    public Result<String> upload(MultipartFile file) {
        // 上传文件到OSS
        try {
            String fileName = aliOssUtil.upload(file.getBytes(), file.getOriginalFilename());
            return Result.success(fileName);
        } catch (Exception e) {
            throw new UploadFileFailException(MessageConstant.UPLOAD_FAILED);
        }
    }
}
