package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 密码错误异常
 */
public class PasswordErrorException extends BaseException {

    public PasswordErrorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PasswordErrorException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
