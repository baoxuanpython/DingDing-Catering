package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 文件上传失败异常
 */
public class UploadFileFailException extends BaseException {

    public UploadFileFailException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UploadFileFailException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
