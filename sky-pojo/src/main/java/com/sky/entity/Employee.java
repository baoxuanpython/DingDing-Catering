package com.sky.entity;

import io.github.vipxieliang.validx.annotations.ChineseIdCard;
import io.github.vipxieliang.validx.annotations.ChinesePhone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    @NotBlank(message = "手机号不能为空")
    @ChinesePhone(message = "请输入正确的手机号")
    private String phone;

    private String sex;

    @NotBlank(message = "身份证号不能为空")
    @ChineseIdCard(message = "请输入正确的身份证号")
    private String idNumber;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
