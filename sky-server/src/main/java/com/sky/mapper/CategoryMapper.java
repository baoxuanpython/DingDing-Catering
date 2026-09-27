package com.sky.mapper;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void addCategory(Category category);

    void deleteCategory(Long id);

    List<Category> listQuery(Integer type);

    void updateStatus(Integer status, Long id);

    void updateCategory(Category category);
}
