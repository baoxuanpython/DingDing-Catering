package com.sky.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    /**
     * 分页查询分类
     * @param categoryPageQueryDTO 分页查询参数
     * @return 分页结果
     */
    PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void addCategory(CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    List<Category> listQuery(Integer type);

    void updateStatus(Integer status, Long id);

    void updateCategory(CategoryDTO categoryDTO);
}
