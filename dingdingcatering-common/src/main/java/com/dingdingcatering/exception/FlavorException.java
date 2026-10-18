package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

public class FlavorException extends BaseException {
    public FlavorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FlavorException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
