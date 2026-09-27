package com.sky.constant;

/**
 * 密码常量
 */
public class PasswordConstant {

    public static final String DEFAULT_PASSWORD = "CQTK123456";

    public static final String DEFAULT_PASSWORD(String lastSixNumberOfIdNumber){
        return "CQTK" + lastSixNumberOfIdNumber.substring(lastSixNumberOfIdNumber.length() - 6, lastSixNumberOfIdNumber.length());
    }
}
