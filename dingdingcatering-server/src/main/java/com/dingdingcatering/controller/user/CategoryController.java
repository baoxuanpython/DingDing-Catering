package com.dingdingcatering.controller.user;

import com.dingdingcatering.entity.Category;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * C端分类控制器
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 查询启用状态的分类列表接口。
     *
     * @param type 分类类型筛选（1-菜品分类，2-套餐分类，不传则返回全部）
     * @return 启用状态的分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> list(@RequestParam(required = false) Integer type) {
        return Result.success(categoryService.list(type));
    }
}
