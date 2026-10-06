package com.dingdingcatering.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.dto.CategoryDTO;
import com.dingdingcatering.dto.CategoryPageQueryDTO;
import com.dingdingcatering.entity.Category;
import com.dingdingcatering.exception.DeletionNotAllowedException;
import com.dingdingcatering.mapper.admin.CategoryMapper;
import com.dingdingcatering.mapper.admin.DishMapper;
import com.dingdingcatering.mapper.admin.SetMealMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.service.admin.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final SetMealMapper setMealMapper;
    private final DishMapper dishMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper, SetMealMapper setMealMapper, DishMapper dishMapper) {
        this.categoryMapper = categoryMapper;
        this.setMealMapper = setMealMapper;
        this.dishMapper = dishMapper;
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
        if (setMealMapper.queryByCategoryId(id) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        if (dishMapper.queryByCategoryId(id) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
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
