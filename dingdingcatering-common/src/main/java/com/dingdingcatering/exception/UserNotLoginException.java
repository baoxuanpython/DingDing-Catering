package com.dingdingcatering.exception;

/**
 * 用户未登录异常
 * <p>
 * 当需要用户登录但当前请求未携带有效身份信息时抛出
 */
public class UserNotLoginException extends BaseException {

    public UserNotLoginException(String msg) {
        super(msg);
    }

}
