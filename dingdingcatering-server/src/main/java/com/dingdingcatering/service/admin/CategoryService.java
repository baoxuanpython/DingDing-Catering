package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.CategoryDTO;
import com.dingdingcatering.dto.CategoryPageQueryDTO;
import com.dingdingcatering.entity.Category;
import com.dingdingcatering.result.PageResult;

import java.util.List;


/**
 * 分类管理服务接口
 */
public interface CategoryService {
    /**
     * 分页查询分类。
     *
     * @param categoryPageQueryDTO 分页查询参数
     * @return 分类分页数据
     */
    PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类。
     *
     * @param categoryDTO 分类信息
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 删除分类。
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 根据类型查询分类列表。
     *
     * @param type 分类类型
     * @return 分类列表
     */
    List<Category> listQuery(Integer type);

    /**
     * 更新分类启用状态。
     *
     * @param status 目标状态
     * @param id     分类ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 更新分类信息。
     *
     * @param categoryDTO 待更新的分类信息
     */
    void updateCategory(CategoryDTO categoryDTO);
}
