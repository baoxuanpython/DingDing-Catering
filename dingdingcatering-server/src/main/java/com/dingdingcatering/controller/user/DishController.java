package com.dingdingcatering.controller.user;

import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.DishService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
public class DishController {
    private final DishService dishService;
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }
    @GetMapping("/list")
    public Result<List<Dish>> list(@RequestParam Integer categoryId) {
        return Result.success(dishService.list(categoryId));
    }
}
