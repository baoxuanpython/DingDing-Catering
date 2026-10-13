package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 业务异常基类
 * <p>
 * 所有业务异常都应携带 {@link ErrorCode}，便于全局异常处理器
 * 精准返回业务错误码，避免使用 code=0 无法区分错误类型的问题。
 */
public class BaseException extends RuntimeException {

    /** 业务错误码 */
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage() + (detail != null && !detail.isEmpty() ? " " + detail : ""));
        this.errorCode = errorCode;
    }

    /**
     * 获取业务错误码
     *
     * @return 错误码枚举
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
