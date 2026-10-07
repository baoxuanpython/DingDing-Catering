package com.dingdingcatering.service.user;

import com.dingdingcatering.dto.UserLoginDTO;
import com.dingdingcatering.vo.UserLoginVO;

public interface UserService {
    UserLoginVO login(UserLoginDTO userLoginDTO);

}
