package com.dingdingcatering.controller.admin;

import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.exception.UploadFileFailException;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class UploadController {
    private final AliOssUtil aliOssUtil;

    public UploadController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    /**
     * 文件上传接口（阿里云OSS）。
     * <p>
     * 处理流程：
     * 1. 接收前端上传的文件
     * 2. 调用阿里云OSS SDK上传文件
     * 3. 返回文件访问URL
     *
     * @param file 待上传的文件
     * @return 文件访问URL
     * @throws UploadFileFailException 当文件上传失败时抛出
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        // 上传文件到OSS
        try {
            String fileName = aliOssUtil.upload(file.getBytes(), file.getOriginalFilename());
            return Result.success(fileName);
        } catch (Exception e) {
            throw new UploadFileFailException(ErrorCode.UPLOAD_FAILED);
        }
    }
}
