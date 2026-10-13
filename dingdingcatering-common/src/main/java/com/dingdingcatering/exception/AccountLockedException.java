package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 账号被锁定异常
 */
public class AccountLockedException extends BaseException {

    public AccountLockedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AccountLockedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
