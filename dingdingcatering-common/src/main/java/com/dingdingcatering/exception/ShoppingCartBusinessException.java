package com.dingdingcatering.exception;

/**
 * 购物车业务异常
 * <p>
 * 当购物车相关操作违反业务规则时抛出（如购物车为空、菜品不存在等）
 */
public class ShoppingCartBusinessException extends BaseException {

    public ShoppingCartBusinessException(String msg) {
        super(msg);
    }

}
