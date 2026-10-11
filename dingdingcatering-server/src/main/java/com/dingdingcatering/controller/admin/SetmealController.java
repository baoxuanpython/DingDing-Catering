package com.dingdingcatering.controller.admin;

import com.dingdingcatering.dto.SetMealDTO;
import com.dingdingcatering.dto.SetMealPageQueryDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.SetmealService;
import com.dingdingcatering.vo.SetMealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 套餐管理控制器（管理端）
 */
@Slf4j
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
public class SetmealController {
    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    /**
     * 套餐分页查询接口。
     *
     * @param setmealPageQueryDTO 分页查询参数（含名称、分类、状态筛选）
     * @return 套餐分页数据
     */
    @GetMapping("/page")
    public Result<PageResult<SetMealVO>> pageQuery(SetMealPageQueryDTO setmealPageQueryDTO) {
        PageResult<SetMealVO> result = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 新增套餐接口。
     *
     * @param setmealDTO 套餐及关联菜品信息
     * @return 添加成功提示
     */
    @PostMapping
    public Result<Void> addSetmeal(@RequestBody SetMealDTO setmealDTO) {
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐接口。
     *
     * @param ids 逗号分隔的套餐ID列表
     * @return 删除成功提示
     */
    @DeleteMapping
    public Result<Void> deleteSetmeal(@RequestParam String ids) {
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }

    /**
     * 根据ID查询套餐详情接口。
     *
     * @param id 用于查询套餐的唯一标识
     * @return 套餐详情（含关联菜品列表）
     */
    @GetMapping("/{id}")
    public Result<SetMealVO> getSetmealById(@PathVariable Long id) {
        SetMealVO setMealVO = setmealService.getSetmealById(id);
        return Result.success(setMealVO);
    }

    /**
     * 更新套餐起售状态接口。
     *
     * @param status 目标状态（0-停用，1-启用）
     * @param id     用于定位目标套餐的主键标识
     * @return 更新成功提示
     */
    @PostMapping("/status/{status}")
    public Result<Void> updateStatus(@PathVariable Integer status ,@RequestParam Long id) {
        setmealService.updateStatus(status,id);
        return Result.success();
    }

    /**
     * 更新套餐信息接口。
     *
     * @param setmealDTO 待更新的套餐及关联菜品信息
     * @return 更新成功提示
     */
    @PutMapping
    public Result<Void> updateSetmeal(@RequestBody SetMealDTO setmealDTO) {
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }
}
