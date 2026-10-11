package com.dingdingcatering.exception;

/**
 * 地址簿业务异常
 * <p>
 * 当地址簿相关操作失败时抛出（如地址不存在、地址数量超限等）
 */
public class AddressBookBusinessException extends BaseException {

    public AddressBookBusinessException(String msg) {
        super(msg);
    }

}
