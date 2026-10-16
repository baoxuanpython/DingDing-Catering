package com.dingdingcatering.controller.admin;


import com.dingdingcatering.dto.DishDTO;
import com.dingdingcatering.dto.DishPageQueryDTO;
import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.DishService;
import com.dingdingcatering.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理控制器（管理端）
 */
@Slf4j
@RestController("adminDishController")
@RequestMapping("/admin/dish")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * 菜品分页查询接口。
     *
     * @param dishPageQueryDTO 分页查询参数（含名称、分类、状态筛选）
     * @return 菜品分页数据
     */
    @GetMapping("/page")
    public Result<PageResult<DishVO>> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageResult<DishVO> pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增菜品接口。
     *
     * @param dishDTO 菜品及口味信息
     * @return 添加成功提示
     */
    @PostMapping
    public Result<Void> addDish(@RequestBody DishDTO dishDTO) {
        dishService.addDish(dishDTO);
        return Result.success();
    }

    /**
     * 批量删除菜品接口。
     *
     * @param ids 逗号分隔的菜品ID列表
     * @return 删除成功提示
     */
    @DeleteMapping
    public Result<Void> deleteDish(@RequestParam String ids) {
        dishService.deleteDish(ids);
        return Result.success();
    }

    /**
     * 修改菜品信息接口。
     *
     * @param dishDTO 待修改的菜品及口味信息
     * @return 修改成功提示
     */
    @PutMapping
    public Result<Void> updateDish(@RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 根据ID查询菜品详情接口。
     *
     * @param id 用于查询菜品的唯一标识
     * @return 菜品详情（含口味列表）
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 根据分类ID查询菜品列表接口。
     *
     * @param categoryId 用于筛选菜品的分类标识
     * @return 该分类下的菜品列表
     */
    @GetMapping("/list")
    public Result<List<Dish>> getList(@RequestParam Long categoryId) {
        List<Dish> list = dishService.getList(categoryId);
        return Result.success(list);
    }

    /**
     * 更新菜品起售状态接口。
     *
     * @param status 目标状态（0-停售，1-起售）
     * @param id     用于定位目标菜品的主键标识
     * @return 更新成功提示
     */
    @PostMapping("/status/{status}")
    public Result<Void> updateStatus(@PathVariable Integer status,@RequestParam Long id) {
        dishService.updateStatus(status ,id);
        return Result.success();
    }
}
