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

/**
 * 分类管理控制器（管理端）
 */
@Slf4j
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 分类分页查询接口。
     *
     * @param categoryPageQueryDTO 分页查询参数（含名称、类型筛选）
     * @return 分类分页数据
     */
    @GetMapping("/page")
    public Result<PageResult<Category>> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        return Result.success(categoryService.pageQuery(categoryPageQueryDTO));
    }

    /**
     * 新增分类接口。
     *
     * @param categoryDTO 分类信息
     * @return 添加成功提示
     */
    @PostMapping
    public Result<Void> addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类接口。
     *
     * @param id 用于定位目标分类的主键标识
     * @return 删除成功提示
     */
    @DeleteMapping
    public Result<Void> deleteCategory(@RequestParam Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类列表接口。
     *
     * @param type 分类类型（1-菜品分类，2-套餐分类）
     * @return 该类型下的分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> listCategory(@RequestParam(required = false) Integer type) {
        return Result.success(categoryService.listQuery(type));
    }

    /**
     * 更新分类启用状态接口。
     *
     * @param status 目标状态（0-禁用，1-启用）
     * @param id     用于定位目标分类的主键标识
     * @return 更新成功提示
     */
    @PostMapping("/status/{status}")
    public Result<Void> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        categoryService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 更新分类信息接口。
     *
     * @param categoryDTO 待更新的分类信息
     * @return 更新成功提示
     */
    @PutMapping
    public Result<Void> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }
}
