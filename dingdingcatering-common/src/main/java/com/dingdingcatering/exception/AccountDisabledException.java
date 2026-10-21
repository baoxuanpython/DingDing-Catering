package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 账号被锁定异常
 */
public class AccountDisabledException extends BaseException {

    public AccountDisabledException(ErrorCode errorCode) {
        super(errorCode);
    }

}
