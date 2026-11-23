package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

public class CategoryException extends BaseException {
    public CategoryException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}