package com.dingdingcatering.dto;

import io.github.vipxieliang.validx.annotations.ChineseIdCard;
import io.github.vipxieliang.validx.annotations.ChinesePhone;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String name;

    @NotBlank(message = "手机号不能为�?)
    @ChinesePhone(message = "请输入正确的手机�?)
    private String phone;

    private String sex;

    @NotBlank(message = "身份证号不能为空")
    @ChineseIdCard(message = "请输入正确的身份证号")
    private String idNumber;

}
