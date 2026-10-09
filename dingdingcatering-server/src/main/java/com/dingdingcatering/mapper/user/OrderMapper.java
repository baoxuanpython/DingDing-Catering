package com.dingdingcatering.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component("userOrderMapper")
@Mapper
public interface OrderMapper {
    void sendReminder(Long id);
}
