package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.entity.Category;
import com.dingdingcatering.mapper.user.CategoryMapper;
import com.dingdingcatering.service.user.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "category", key = "#type != null ? 'type:' + #type : 'list'", unless = "#result == null || #result.isEmpty()")
    public List<Category> list(Integer type) {
        log.info("缓存未命中，查询分类列表，type: {}", type);
        return categoryMapper.listByType(type);
    }

}
