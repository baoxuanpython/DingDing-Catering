package com.dingdingcatering.service.user.Impl;

import com.dingdingcatering.entity.Category;
import com.dingdingcatering.mapper.user.CategoryMapper;
import com.dingdingcatering.service.user.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userCategoryServiceImpl")
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.listByType(type);
    }
}
