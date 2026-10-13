package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 账号不存在异常
 */
public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AccountNotFoundException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
