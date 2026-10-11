package com.dingdingcatering.exception;

/**
 * 文件上传失败异常
 * <p>
 * 当文件上传到 OSS 或其他存储服务失败时抛出
 */
public class UploadFileFailException extends BaseException {

    public UploadFileFailException(String message) {
        super(message);
    }
}
