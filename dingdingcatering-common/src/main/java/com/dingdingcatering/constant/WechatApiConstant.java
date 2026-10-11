package com.dingdingcatering.constant;

/**
 * 微信 API 相关常量
 * <p>
 * 定义微信登录接口地址及请求参数名，用于调用微信 jscode2session 接口获取 OpenID。
 */
public class WechatApiConstant {
    /** 微信登录接口地址 */
    public static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    /** 授权类型参数名 */
    public static final String GRANT_TYPE = "grant_type";
    /** 授权类型参数值 */
    public static final String GRANT_TYPE_VALUE = "authorization_code";
    /** 临时登录凭证参数名 */
    public static final String JS_CODE = "js_code";
    /** 小程序 AppID 参数名 */
    public static final String APPID = "appid";
    /** 小程序 AppSecret 参数名 */
    public static final String SECRET = "secret";
}
