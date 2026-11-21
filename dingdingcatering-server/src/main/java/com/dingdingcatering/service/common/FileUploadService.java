package com.dingdingcatering.service.common;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务接口
 * <p>
 * 管理端与用户端共用同一套上传校验与落盘逻辑，避免两端规则不一致。
 */
public interface FileUploadService {

    /**
     * 校验并上传文件到阿里云 OSS。
     *
     * @param file 待上传的文件
     * @return 文件访问 URL
     */
    String upload(MultipartFile file);
}