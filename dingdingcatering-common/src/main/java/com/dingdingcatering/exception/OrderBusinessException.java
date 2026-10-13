package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 订单业务异常
 */
public class OrderBusinessException extends BaseException {

    public OrderBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OrderBusinessException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
