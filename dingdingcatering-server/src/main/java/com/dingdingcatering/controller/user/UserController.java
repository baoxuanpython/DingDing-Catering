package com.dingdingcatering.controller.user;

import com.dingdingcatering.dto.UserLoginDTO;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.UserService;

import com.dingdingcatering.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C端用户控制器
 */
@RestController
@RequestMapping("/user/user")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录接口（微信授权登录）。
     * <p>
     * 处理流程：
     * 1. 接收微信登录code
     * 2. 调用微信API获取openid
     * 3. 自动注册新用户（如首次登录）
     * 4. 生成JWT令牌并返回
     *
     * @param userLoginDTO 微信登录code
     * @return 用户信息及JWT令牌
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        return Result.success(userService.login(userLoginDTO));
    }

    /**
     * 用户退出登录接口。
     *
     * @return 退出成功提示
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
}
