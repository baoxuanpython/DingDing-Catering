package com.sky.controller;

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
    public Result<PageResult<Category>> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        return Result.success(categoryService.pageQuery(categoryPageQueryDTO));
    }
    @PostMapping
    public Result<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        log.info("添加{}成功", categoryDTO.getName());
        return Result.success(String.format("添加%s成功", categoryDTO.getName()));
    }
    @DeleteMapping
    public Result<String> deleteCategory(@RequestParam Long id) {
        categoryService.deleteCategory(id);
        log.info("删除{}成功", id);
        return Result.success(String.format("删除%s成功", id));
    }
    @GetMapping("/list")
    public Result<List<Category>> listCategory(@RequestParam Integer type) {
        return Result.success(categoryService.listQuery(type));
    }
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("更新分类状态：id={}, status={}", id, status == 1 ? "启用" : "停用");
        categoryService.updateStatus(status, id);
        return Result.success(status == 1 ? "启用成功" : "停用成功");
    }
    @PutMapping
    public Result<String> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        log.info("更新{}成功", categoryDTO.getName());
        return Result.success(String.format("更新%s成功", categoryDTO.getName()));
    }
}
