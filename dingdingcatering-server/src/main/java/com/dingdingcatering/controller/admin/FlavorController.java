package com.dingdingcatering.controller.admin;

import com.dingdingcatering.dto.FlavorDTO;
import com.dingdingcatering.dto.FlavorPageQueryDTO;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.FlavorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminFlavorController")
@RequestMapping("/admin/flavor")
public class FlavorController {
    private final FlavorService flavorService;

    public FlavorController(FlavorService flavorService) {
        this.flavorService = flavorService;
    }

    @PostMapping
    public Result<Void> addFlavor(@RequestBody FlavorDTO flavorDTO) {
        flavorService.addFlavor(flavorDTO);
        return Result.success();
    }

    @PutMapping
    public Result<Void> updateFlavor(@RequestBody FlavorDTO flavorDTO) {
        flavorService.updateFlavor(flavorDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult<FlavorDTO>> pageFlavor(FlavorPageQueryDTO flavorPageQueryDTO) {
        return Result.success(flavorService.pageQueryFlavor(flavorPageQueryDTO));
    }

    @GetMapping("/{id}")
    public Result<FlavorDTO> getFlavor(@PathVariable Long id) {
        return Result.success(flavorService.getById(id));
    }

    @GetMapping("/list")
    public Result<List<FlavorDTO>> listFlavor() {
        return Result.success(flavorService.listFlavor());
    }

    @DeleteMapping
    public Result<Void> deleteFlavor(@RequestParam Long id) {
        flavorService.deleteFlavor(id);
        return Result.success();
    }
}