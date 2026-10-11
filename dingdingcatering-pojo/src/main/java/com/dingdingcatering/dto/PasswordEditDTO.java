package com.dingdingcatering.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 密码修改数据传输对象
 */
@Data
public class PasswordEditDTO implements Serializable {

    /** 员工ID */
    private Long empId;

    /** 旧密码 */
    private String oldPassword;

    /** 新密码 */
    private String newPassword;

}
