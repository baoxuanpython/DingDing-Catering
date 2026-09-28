package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
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
