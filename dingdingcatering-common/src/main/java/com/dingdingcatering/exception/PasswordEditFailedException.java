package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 密码修改失败异常
 */
public class PasswordEditFailedException extends BaseException {

    public PasswordEditFailedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PasswordEditFailedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
