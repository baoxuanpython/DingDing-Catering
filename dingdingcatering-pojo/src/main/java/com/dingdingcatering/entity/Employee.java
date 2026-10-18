package com.dingdingcatering.entity;

import com.dingdingcatering.annotation.AutoFill;
import com.dingdingcatering.enumeration.OperationType;
import io.github.vipxieliang.validx.annotations.ChineseIdCard;
import io.github.vipxieliang.validx.annotations.ChinesePhone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体类（对应数据库表：employee）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AutoFill
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 员工主键 */
    private Long id;

    /** 登录用户名 */
    private String username;

    /** 员工姓名 */
    private String name;

    /** 登录密码 */
    private String password;

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

    /** 账号状态 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人ID */
    private Long createUser;

    /** 更新人ID */
    private Long updateUser;

    /** 创建人姓名（冗余字段） */
    private String createEmployeeName;

    /** 更新人姓名（冗余字段） */
    private String updateEmployeeName;

}