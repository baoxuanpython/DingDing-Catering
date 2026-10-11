package com.dingdingcatering.constant;

/**
 * JWT Claims 字段常量
 * <p>
 * 定义 JWT Token 中携带的声明字段名，用于登录时构建 Token 和拦截器中解析用户信息。
 */
public class JwtClaimsConstant {

    /** 员工 ID 字段 */
    public static final String EMP_ID = "empId";
    /** 用户 ID 字段 */
    public static final String USER_ID = "id";
    /** 手机号字段 */
    public static final String PHONE = "phone";
    /** 用户名字段 */
    public static final String USERNAME = "username";
    /** 姓名字段 */
    public static final String NAME = "name";
    /** 微信 OpenID 字段 */
    public static final String OPENID = "openid";
}
