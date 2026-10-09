package com.dingdingcatering.controller.admin;

import com.dingdingcatering.dto.CategoryDTO;
import com.dingdingcatering.dto.CategoryPageQueryDTO;
import com.dingdingcatering.entity.Category;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/page")
    public Result<PageResult<Category>> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        return Result.success(categoryService.pageQuery(categoryPageQueryDTO));
    }
    @PostMapping
    public Result<Void> addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }
    @DeleteMapping
    public Result<Void> deleteCategory(@RequestParam Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<Category>> listCategory(@RequestParam Integer type) {
        return Result.success(categoryService.listQuery(type));
    }
    @PostMapping("/status/{status}")
    public Result<Void> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        categoryService.updateStatus(status, id);
        return Result.success();
    }
    @PutMapping
    public Result<Void> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }
}
