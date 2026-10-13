package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 令牌无效异常
 */
public class TokenInvalidException extends BaseException {

    public TokenInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenInvalidException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
