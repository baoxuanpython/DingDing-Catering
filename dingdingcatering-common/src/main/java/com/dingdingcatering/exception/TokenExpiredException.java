package com.dingdingcatering.exception;

/**
 * JWT令牌过期异常
 */
public class TokenExpiredException extends BaseException {

    public TokenExpiredException(String msg) {
        super(msg);
    }
}
