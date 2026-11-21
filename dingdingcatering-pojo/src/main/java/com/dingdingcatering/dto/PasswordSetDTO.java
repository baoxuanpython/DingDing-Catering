package com.dingdingcatering.dto;

import com.dingdingcatering.constant.PasswordConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员修改指定员工密码的数据传输对象
 * <p>
 * 仅管理员可调用，目标员工由路径参数指定，因此不包含员工ID；
 * 管理员拥有最高权限，无需提供旧密码。
 */
@Data
public class PasswordSetDTO implements Serializable {

    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    @Size(min = PasswordConstant.MIN_LENGTH, max = PasswordConstant.MAX_LENGTH,
            message = "新密码长度需在" + PasswordConstant.MIN_LENGTH + "到" + PasswordConstant.MAX_LENGTH + "位之间")
    @Pattern(regexp = PasswordConstant.PATTERN, message = "新密码不能包含空格")
    private String newPassword;

}