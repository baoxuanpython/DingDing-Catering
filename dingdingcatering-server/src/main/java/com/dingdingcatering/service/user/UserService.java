package com.dingdingcatering.service.user;

import com.dingdingcatering.dto.UserEditDTO;
import com.dingdingcatering.dto.UserLoginDTO;
import com.dingdingcatering.entity.User;
import com.dingdingcatering.vo.UserLoginVO;

/**
 * C端用户服务接口
 */
public interface UserService {
    /**
     * 微信授权登录。
     *
     * @param userLoginDTO 微信登录code
     * @return 用户信息及JWT令牌
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

    /**
     * 查询当前用户信息。
     *
     * @return 用户信息
     */
    User info();

    /**
     * 修改当前用户信息。
     *
     * @param userEditDTO 需修改的字段（所有字段可选）
     * @return 修改后的用户信息
     */
    User updateInfo(UserEditDTO userEditDTO);
}