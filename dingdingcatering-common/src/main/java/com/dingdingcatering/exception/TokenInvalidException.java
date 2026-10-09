package com.dingdingcatering.exception;

/**
 * JWT令牌无效异常
 */
public class TokenInvalidException extends BaseException {

    public TokenInvalidException(String msg) {
        super(msg);
    }
}
