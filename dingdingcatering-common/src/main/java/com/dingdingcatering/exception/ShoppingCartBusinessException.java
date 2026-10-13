package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 购物车业务异常
 */
public class ShoppingCartBusinessException extends BaseException {

    public ShoppingCartBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ShoppingCartBusinessException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
