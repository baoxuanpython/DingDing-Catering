package com.dingdingcatering.controller.user;

import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.DishService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * C端菜品控制器
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
public class DishController {
    private final DishService dishService;
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * 根据分类ID查询起售菜品列表接口。
     *
     * @param categoryId 用于筛选菜品的分类标识
     * @return 该分类下已起售的菜品列表
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(@RequestParam Integer categoryId) {
        return Result.success(dishService.list(categoryId));
    }
}
