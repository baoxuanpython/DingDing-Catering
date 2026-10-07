package com.dingdingcatering.controller.user;

import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.SetmealService;
import com.dingdingcatering.vo.DishItemVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
public class SetmealController {

    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    @GetMapping("/list")
    public Result<List<Setmeal>> list(@RequestParam Integer categoryId) {
        return Result.success(setmealService.list(categoryId));
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> dish(@PathVariable Long id) {
        return Result.success(setmealService.dish(id));
    }
}
