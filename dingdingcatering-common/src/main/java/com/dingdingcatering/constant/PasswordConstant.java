package com.dingdingcatering.constant;

/**
 * 密码常量
 * <p>
 * 统一定义密码长度约束与格式规则，供员工自助改密、管理员修改员工密码两处共用，
 * 避免规则分散后出现不一致。
 */
public class PasswordConstant {

    /** 密码最小长度 */
    public static final int MIN_LENGTH = 6;

    /** 密码最大长度 */
    public static final int MAX_LENGTH = 32;

    /** 密码格式：至少一个非空白字符，即禁止纯空格或夹带空格 */
    public static final String PATTERN = "^\\S+$";

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