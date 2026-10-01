package com.sky.controller;

import com.sky.annotation.AutoLogDTO;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/page")
    @AutoLogDTO("分页查询分类")
    public Result<PageResult<Category>> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        return Result.success(categoryService.pageQuery(categoryPageQueryDTO));
    }
    @PostMapping
    @AutoLogDTO("新增分类")
    public Result<Void> addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }
    @DeleteMapping
    @AutoLogDTO("删除分类")
    public Result<Void> deleteCategory(@RequestParam Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
    @GetMapping("/list")
    @AutoLogDTO("查询分类列表")
    public Result<List<Category>> listCategory(@RequestParam Integer type) {
        return Result.success(categoryService.listQuery(type));
    }
    @PostMapping("/status/{status}")
    @AutoLogDTO("更新分类状态")
    public Result<Void> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        categoryService.updateStatus(status, id);
        return Result.success();
    }
    @PutMapping
    @AutoLogDTO("编辑分类")
    public Result<Void> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }
}
