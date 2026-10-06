package com.dingdingcatering.controller.admin;

import com.dingdingcatering.annotation.AutoLogDTO;
import com.dingdingcatering.dto.SetMealDTO;
import com.dingdingcatering.dto.SetMealPageQueryDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.SetmealService;
import com.dingdingcatering.vo.SetMealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {
    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @GetMapping("/page")
    @AutoLogDTO("分页查询套餐")
    public Result<PageResult<SetMealVO>> pageQuery(SetMealPageQueryDTO setmealPageQueryDTO) {
        PageResult<SetMealVO> result = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(result);
    }

    @PostMapping
    @AutoLogDTO("新增套餐")
    public Result<Void> addSetmeal(@RequestBody SetMealDTO setmealDTO) {
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    @DeleteMapping
    @AutoLogDTO("删除套餐")
    public Result<Void> deleteSetmeal(@RequestParam String ids) {
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @AutoLogDTO("根据套餐id查询套餐详情")
    public Result<SetMealVO> getSetmealById(@PathVariable Long id) {
        SetMealVO setMealVO = setmealService.getSetmealById(id);
        return Result.success(setMealVO);
    }

    @PostMapping("/status/{status}")
    @AutoLogDTO("更新套餐状态")
    public Result<Void> updateStatus(@PathVariable Integer status ,@RequestParam Long id) {
        setmealService.updateStatus(status,id);
        return Result.success();
    }
    @PutMapping
    @AutoLogDTO("更新套餐")
    public Result<Void> updateSetmeal(@RequestBody SetMealDTO setmealDTO) {
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }
}
