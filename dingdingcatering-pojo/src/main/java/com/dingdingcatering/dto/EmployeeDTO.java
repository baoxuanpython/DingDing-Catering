package com.dingdingcatering.dto;

import io.github.vipxieliang.validx.annotations.ChineseIdCard;
import io.github.vipxieliang.validx.annotations.ChinesePhone;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 员工数据传输对象（用于新增/编辑员工）
 */
@Data
public class EmployeeDTO implements Serializable {

    /** 员工主键 */
    private Long id;

    /** 登录用户名 */
    private String username;

    /** 员工姓名 */
    private String name;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    @ChinesePhone(message = "请输入正确的手机号")
    private String phone;

    /** 性别 */
    private String sex;

    /** 身份证号 */
    @NotBlank(message = "身份证号不能为空")
    @ChineseIdCard(message = "请输入正确的身份证号")
    private String idNumber;

}
