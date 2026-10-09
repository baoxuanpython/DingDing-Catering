package com.dingdingcatering.controller.admin;

import com.dingdingcatering.dto.SetMealDTO;
import com.dingdingcatering.dto.SetMealPageQueryDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.SetmealService;
import com.dingdingcatering.vo.SetMealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
public class SetmealController {
    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @GetMapping("/page")
    public Result<PageResult<SetMealVO>> pageQuery(SetMealPageQueryDTO setmealPageQueryDTO) {
        PageResult<SetMealVO> result = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(result);
    }

    @PostMapping
    public Result<Void> addSetmeal(@RequestBody SetMealDTO setmealDTO) {
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> deleteSetmeal(@RequestParam String ids) {
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SetMealVO> getSetmealById(@PathVariable Long id) {
        SetMealVO setMealVO = setmealService.getSetmealById(id);
        return Result.success(setMealVO);
    }

    @PostMapping("/status/{status}")
    public Result<Void> updateStatus(@PathVariable Integer status ,@RequestParam Long id) {
        setmealService.updateStatus(status,id);
        return Result.success();
    }
    @PutMapping
    public Result<Void> updateSetmeal(@RequestBody SetMealDTO setmealDTO) {
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }
}
