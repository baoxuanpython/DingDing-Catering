package com.dingdingcatering.controller.admin;


import com.dingdingcatering.dto.DishDTO;
import com.dingdingcatering.dto.DishPageQueryDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.DishService;
import com.dingdingcatering.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("adminDishController")
@RequestMapping("/admin/dish")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/page")
    public Result<PageResult<DishVO>> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageResult<DishVO> pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result<Void> addDish(@RequestBody DishDTO dishDTO) {
        dishService.addDish(dishDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> deleteDish(@RequestParam String ids) {
        dishService.deleteDish(ids);
        return Result.success();
    }

    @PutMapping
    public Result<Void> updateDish(@RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    @GetMapping("/list")
    public Result<List<DishVO>> getList(@RequestParam Long categoryId) {
        List<DishVO> list = dishService.getList(categoryId);
        return Result.success(list);
    }

    @PostMapping("/status/{status}")
    public Result<Void> updateStatus(@PathVariable Integer status,@RequestParam Long id) {
        dishService.updateStatus(status ,id);
        return Result.success();
    }
}
