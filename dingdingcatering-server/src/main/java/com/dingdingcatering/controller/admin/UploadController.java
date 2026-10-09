package com.dingdingcatering.controller.admin;

import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.exception.UploadFileFailException;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.utils.AliOssUtil;
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
