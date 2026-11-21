package com.dingdingcatering.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {

    /** 用户主键 */
    private Long id;

    /** 微信OpenID */
    private String openid;

    /** 昵称 */
    private String name;

    /** 头像URL */
    private String avatar;

    /** JWT令牌 */
    private String token;

}