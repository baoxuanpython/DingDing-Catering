package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User login(String openid);

    void createUser(User user);

    User getById(Long id, String openid);
}
