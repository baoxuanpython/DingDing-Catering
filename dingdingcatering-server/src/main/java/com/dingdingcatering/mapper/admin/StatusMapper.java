package com.dingdingcatering.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StatusMapper {

    String getValue(@Param("configKey") String configKey);

    int updateValue(@Param("configKey") String configKey, @Param("configValue") String configValue);
}
