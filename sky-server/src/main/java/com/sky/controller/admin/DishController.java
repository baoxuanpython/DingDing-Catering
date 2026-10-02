package com.sky.controller.admin;


import com.sky.annotation.AutoLogDTO;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/page")
    @AutoLogDTO("分页查询菜品")
    public Result<PageResult<DishVO>> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageResult<DishVO> pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    @AutoLogDTO("新增菜品")
    public Result<Void> addDish(@RequestBody DishDTO dishDTO) {
        dishService.addDish(dishDTO);
        return Result.success();
    }

    @DeleteMapping
    @AutoLogDTO("删除菜品")
    public Result<Void> deleteDish(@RequestParam String ids) {
        dishService.deleteDish(ids);
        return Result.success();
    }

    @PutMapping
    @AutoLogDTO("更新菜品")
    public Result<Void> updateDish(@RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @AutoLogDTO("根据菜品id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    @GetMapping("/list")
    @AutoLogDTO("根据分类id查询菜品")
    public Result<List<DishVO>> getList(@RequestParam Long categoryId) {
        List<DishVO> list = dishService.getList(categoryId);
        return Result.success(list);
    }

    @PostMapping("/status/{status}")
    @AutoLogDTO("修改菜品状态")
    public Result<Void> updateStatus(@PathVariable Integer status,@RequestParam Long id) {
        dishService.updateStatus(status ,id);
        return Result.success();
    }
}
