package com.dingdingcatering.mapper.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ShopConfigMapper {

    @Select("SELECT config_value FROM shop_config WHERE config_key = #{configKey}")
    String getValue(@Param("configKey") String configKey);

    @Update("UPDATE shop_config SET config_value = #{configValue} WHERE config_key = #{configKey}")
    int updateValue(@Param("configKey") String configKey, @Param("configValue") String configValue);
}
