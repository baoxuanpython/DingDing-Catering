package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

public class UnauthorizedOperationException extends BaseException {
    public UnauthorizedOperationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedOperationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
