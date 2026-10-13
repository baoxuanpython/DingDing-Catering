package com.dingdingcatering.exception;

import com.dingdingcatering.enumeration.ErrorCode;

/**
 * 地址簿业务异常
 */
public class AddressBookBusinessException extends BaseException {

    public AddressBookBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AddressBookBusinessException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
