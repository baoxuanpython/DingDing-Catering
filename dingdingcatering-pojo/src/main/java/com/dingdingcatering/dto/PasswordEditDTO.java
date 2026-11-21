package com.dingdingcatering.dto;

import com.dingdingcatering.constant.PasswordConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 密码修改数据传输对象（仅用于员工自助修改本人密码）
 * <p>
 * 目标员工由服务端从令牌中解析，因此不包含员工ID，避免被用来修改他人密码。
 */
@Data
public class PasswordEditDTO implements Serializable {

    /** 旧密码 */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    @Size(min = PasswordConstant.MIN_LENGTH, max = PasswordConstant.MAX_LENGTH,
            message = "新密码长度需在" + PasswordConstant.MIN_LENGTH + "到" + PasswordConstant.MAX_LENGTH + "位之间")
    @Pattern(regexp = PasswordConstant.PATTERN, message = "新密码不能包含空格")
    private String newPassword;

}