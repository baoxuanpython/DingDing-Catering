package com.dingdingcatering.service.user;

import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.vo.DishItemVO;

import java.util.List;

/**
 * C端套餐服务接口
 */
public interface SetmealService {
    /**
     * 根据分类ID查询起售套餐列表。
     *
     * @param categoryId 分类ID
     * @return 已启用的套餐列表
     */
    List<Setmeal> list(Integer categoryId);

    /**
     * 查询套餐内菜品列表。
     *
     * @param id 套餐ID
     * @return 套餐包含的菜品项列表
     */
    List<DishItemVO> dish(Long id);
}
