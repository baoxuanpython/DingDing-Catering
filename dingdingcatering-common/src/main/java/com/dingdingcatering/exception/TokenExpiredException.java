package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 令牌过期异常
 */
public class TokenExpiredException extends BaseException {

    public TokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenExpiredException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
