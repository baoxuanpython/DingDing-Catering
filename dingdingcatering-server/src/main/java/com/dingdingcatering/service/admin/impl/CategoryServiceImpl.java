package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.annotation.AutoClearCache;
import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.enumeration.CacheType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

@Service("adminCategoryServiceImpl")
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final SetMealMapper setMealMapper;
    private final DishMapper dishMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper,
                               SetMealMapper setMealMapper,
                               DishMapper dishMapper) {
        this.categoryMapper = categoryMapper;
        this.setMealMapper = setMealMapper;
        this.dishMapper = dishMapper;
    }

    @Override
    public PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.debug("分页查询分类: page={}, pageSize={}, type={}",
                categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize(), categoryPageQueryDTO.getType());
        try (Page<Category> page = PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize())) {
            List<Category> list = categoryMapper.pageQuery(categoryPageQueryDTO);
            log.debug("分页查询分类完成: total={}", page.getTotal());
            return new PageResult<>(page.getTotal(), list);
        }
    }

    @Override
    @AutoClearCache(CacheType.CATEGORY)
    public void addCategory(CategoryDTO categoryDTO) {
        log.info("新增分类: name={}, type={}", categoryDTO.getName(), categoryDTO.getType());
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(1);
        categoryMapper.addCategory(category);
        log.info("新增分类成功: id={}", category.getId());
    }

    @Override
    @AutoClearCache(CacheType.CATEGORY)
    public void deleteCategory(Long id) {
        log.info("删除分类: id={}", id);
        if (setMealMapper.queryByCategoryId(id) > 0) {
            log.warn("删除分类失败: 分类被套餐关联, id={}", id);
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        if (dishMapper.queryByCategoryId(id) > 0) {
            log.warn("删除分类失败: 分类被菜品关联, id={}", id);
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        categoryMapper.deleteCategory(id);
        log.info("删除分类成功: id={}", id);
    }

    @Override
    public List<Category> listQuery(Integer type) {
        log.debug("查询分类列表: type={}", type);
        return categoryMapper.listQuery(type);
    }

    @Override
    @AutoClearCache(CacheType.CATEGORY)
    public void updateStatus(Integer status, Long id) {
        log.info("更新分类状态: id={}, status={}", id, status);
        Category category = new Category();
        category.setStatus(status);
        category.setId(id);
        categoryMapper.updateCategory(category);
    }

    @Override
    @AutoClearCache(CacheType.CATEGORY)
    public void updateCategory(CategoryDTO categoryDTO) {
        log.info("更新分类: id={}, name={}", categoryDTO.getId(), categoryDTO.getName());
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateCategory(category);
        log.info("更新分类成功: id={}", categoryDTO.getId());
    }
}
