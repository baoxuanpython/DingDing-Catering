package com.dingdingcatering.service.user;

import com.dingdingcatering.dto.UserLoginDTO;
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

}
