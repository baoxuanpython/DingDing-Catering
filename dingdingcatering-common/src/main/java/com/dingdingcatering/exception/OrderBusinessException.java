package com.dingdingcatering.exception;

/**
 * 订单业务异常
 * <p>
 * 当订单相关操作违反业务规则时抛出（如订单状态错误、下单条件不满足等）
 */
public class OrderBusinessException extends BaseException {

    public OrderBusinessException(String msg) {
        super(msg);
    }

}
