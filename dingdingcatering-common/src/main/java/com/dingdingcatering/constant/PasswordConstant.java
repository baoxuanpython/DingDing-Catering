package com.dingdingcatering.constant;

/**
 * 密码常量
 * <p>
 * 定义默认密码生成规则：前缀 "CQTK" + 身份证号后六位
 */
public class PasswordConstant {

    /**
     * 生成默认密码
     *
     * @param lastSixNumberOfIdNumber 身份证号后六位
     * @return 默认密码（格式：CQTK + 身份证号后六位）
     */
    public static String DEFAULT_PASSWORD(String lastSixNumberOfIdNumber){
        return "CQTK" + lastSixNumberOfIdNumber.substring(lastSixNumberOfIdNumber.length() - 6);
    }
}
