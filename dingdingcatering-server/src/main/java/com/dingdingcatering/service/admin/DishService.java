package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.DishDTO;
import com.dingdingcatering.dto.DishPageQueryDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.vo.DishVO;

import java.util.List;

/**
 * 菜品管理服务接口
 */
public interface DishService {

    /**
     * 菜品分页查询。
     *
     * @param dishPageQueryDTO 分页查询参数
     * @return 菜品分页数据
     */
    PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID查询菜品详情。
     *
     * @param id 菜品ID
     * @return 菜品详情（含口味列表）
     */
    DishVO getById(Long id);

    /**
     * 根据分类ID查询菜品列表。
     *
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    List<DishVO> getList(Long categoryId);

    /**
     * 新增菜品（含口味）。
     *
     * @param dishDTO 菜品及口味信息
     */
    void addDish(DishDTO dishDTO);

    /**
     * 批量删除菜品。
     *
     * @param ids 逗号分隔的菜品ID列表
     */
    void deleteDish(String ids);

    /**
     * 更新菜品信息（含口味）。
     *
     * @param dishDTO 待更新的菜品信息
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 更新菜品起售状态。
     *
     * @param status 目标状态
     * @param id     菜品ID
     */
    void updateStatus(Integer status, Long id);
}
