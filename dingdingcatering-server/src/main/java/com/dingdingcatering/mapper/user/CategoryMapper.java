package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("userCategoryMapper")
@Mapper
public interface CategoryMapper {
    List<Category> listByType(Integer type);
}
