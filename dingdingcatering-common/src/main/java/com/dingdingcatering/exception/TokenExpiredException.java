package com.dingdingcatering.exception;

/**
 * JWT令牌过期异常
 */
public class TokenExpiredException extends BaseException {

    public TokenExpiredException() {
        super("JWT令牌已过期");
    }

    public TokenExpiredException(String msg) {
        super(msg);
    }
}
