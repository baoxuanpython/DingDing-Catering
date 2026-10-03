package com.sky.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * 密码常量
 */
@Slf4j
public class PasswordConstant {

    public static final String DEFAULT_PASSWORD = "CQTK123456";

    public static final String DEFAULT_PASSWORD(String lastSixNumberOfIdNumber){
        return "CQTK" + lastSixNumberOfIdNumber.substring(lastSixNumberOfIdNumber.length() - 6);
    }
}
