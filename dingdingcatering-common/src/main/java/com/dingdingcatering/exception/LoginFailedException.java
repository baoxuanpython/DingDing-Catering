package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 登录失败异常
 */
public class LoginFailedException extends BaseException {

    public LoginFailedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public LoginFailedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
