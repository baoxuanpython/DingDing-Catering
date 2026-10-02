package com.sky.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.admin.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.admin.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        try (Page<Category> page = PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize())) {
            List<Category> list = categoryMapper.pageQuery(categoryPageQueryDTO);
            return new PageResult<>(page.getTotal(), list);
        }
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(1);
        categoryMapper.addCategory(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteCategory(id);
    }

    @Override
    public List<Category> listQuery(Integer type) {
        return categoryMapper.listQuery(type);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Category category = new Category();
        category.setStatus(status);
        category.setId(id);
        categoryMapper.updateCategory(category);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateCategory(category);
    }

}
