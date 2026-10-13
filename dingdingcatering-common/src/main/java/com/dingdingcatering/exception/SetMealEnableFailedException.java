package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 套餐启用失败异常
 */
public class SetMealEnableFailedException extends BaseException {

    public SetMealEnableFailedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SetMealEnableFailedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
