package com.dingdingcatering.service.user;

import com.dingdingcatering.vo.DishVO;

import java.util.List;

/**
 * C端菜品服务接口
 */
public interface DishService {
    /**
     * 根据分类ID查询起售菜品列表。
     *
     * @param categoryId 分类ID
     * @return 已起售的菜品列表（含分类名和口味）
     */
    List<DishVO> list(Long categoryId);
}