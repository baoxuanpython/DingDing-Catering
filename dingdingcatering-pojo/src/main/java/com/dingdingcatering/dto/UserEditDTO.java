package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户信息编辑数据传输对象
 */
@Data
public class UserEditDTO implements Serializable {

    /** 昵称（可选） */
    private String name;

    /** 头像URL（可选） */
    private String avatar;

    /** 手机号（可选） */
    private String phone;

    /** 性别（可选） */
    private String sex;

    /** 身份证号（可选，仅字符串校验，不调用官方接口） */
    private String idNumber;
}