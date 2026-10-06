package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.annotation.AutoFill;
import com.dingdingcatering.dto.CategoryPageQueryDTO;
import com.dingdingcatering.entity.Category;
import com.dingdingcatering.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @AutoFill
    void addCategory(Category category);

    void deleteCategory(Long id);

    List<Category> listQuery(Integer type);

    @AutoFill(value = OperationType.UPDATE)
    void updateCategory(Category category);
}
