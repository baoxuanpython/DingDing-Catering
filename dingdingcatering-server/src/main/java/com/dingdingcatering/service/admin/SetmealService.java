package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.SetMealDTO;
import com.dingdingcatering.dto.SetMealPageQueryDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.vo.SetMealVO;


/**
 * 套餐管理服务接口
 */
public interface SetmealService {

    /**
     * 套餐分页查询。
     *
     * @param setmealPageQueryDTO 分页查询参数
     * @return 套餐分页数据
     */
    PageResult<SetMealVO> pageQuery(SetMealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐（含关联菜品）。
     *
     * @param setmealDTO 套餐及关联菜品信息
     */
    void addSetmeal(SetMealDTO setmealDTO);

    /**
     * 批量删除套餐。
     *
     * @param ids 逗号分隔的套餐ID列表
     */
    void deleteSetmeal(String ids);

    /**
     * 根据ID查询套餐详情。
     *
     * @param id 套餐ID
     * @return 套餐详情（含关联菜品列表）
     */
    SetMealVO getSetmealById(Long id);

    /**
     * 更新套餐起售状态。
     *
     * @param status 目标状态
     * @param id     套餐ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 更新套餐信息（含关联菜品）。
     *
     * @param setmealDTO 待更新的套餐信息
     */
    void updateSetmeal(SetMealDTO setmealDTO);
}
