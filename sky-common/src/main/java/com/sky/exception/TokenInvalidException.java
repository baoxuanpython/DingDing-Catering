package com.dingdingcatering.exception;

/**
 * JWT令牌无效异常
 */
public class TokenInvalidException extends BaseException {

    public TokenInvalidException() {
        super("JWT令牌无效");
    }

    public TokenInvalidException(String msg) {
        super(msg);
    }
}
