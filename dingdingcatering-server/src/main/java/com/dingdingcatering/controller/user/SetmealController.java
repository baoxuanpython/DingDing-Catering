package com.dingdingcatering.controller.user;

import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.SetmealService;
import com.dingdingcatering.vo.DishItemVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端套餐控制器
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
public class SetmealController {

    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    /**
     * 根据分类ID查询起售套餐列表接口。
     *
     * @param categoryId 用于筛选套餐的分类标识
     * @return 该分类下已启用的套餐列表
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(@RequestParam Integer categoryId) {
        return Result.success(setmealService.list(categoryId));
    }

    /**
     * 查询套餐内菜品列表接口。
     *
     * @param id 用于查询套餐的主键标识
     * @return 套餐包含的菜品项列表
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> dish(@PathVariable Long id) {
        return Result.success(setmealService.dish(id));
    }
}
