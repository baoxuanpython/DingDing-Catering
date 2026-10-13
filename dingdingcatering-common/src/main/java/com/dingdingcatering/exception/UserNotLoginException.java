package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 用户未登录异常
 */
public class UserNotLoginException extends BaseException {

    public UserNotLoginException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserNotLoginException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
