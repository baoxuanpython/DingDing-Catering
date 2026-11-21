package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 上传文件校验失败异常
 * <p>
 * 用于区分「客户端传了非法文件」（如空文件、类型不支持、体积超限）
 * 与「服务端上传到 OSS 失败」两类场景，前者属于客户端错误。
 */
public class UploadFileInvalidException extends BaseException {

    public UploadFileInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UploadFileInvalidException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}