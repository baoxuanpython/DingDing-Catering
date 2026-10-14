package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 套餐无菜品异常
 */
public class SetMealNoDishesException extends BaseException {

    public SetMealNoDishesException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SetMealNoDishesException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
