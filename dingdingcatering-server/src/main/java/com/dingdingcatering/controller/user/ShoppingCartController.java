package com.dingdingcatering.controller.user;

import com.dingdingcatering.dto.ShoppingCartDTO;
import com.dingdingcatering.entity.ShoppingCart;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端购物车控制器
 */
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    /**
     * 添加菜品/套餐到购物车接口。
     *
     * @param shoppingCartDTO 待添加的商品信息（含菜品/套餐ID、口味）
     * @return 添加成功提示
     */
    @PostMapping("/add")
    public Result<Void> addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.editShoppingCart(shoppingCartDTO, true);
        return Result.success();
    }

    /**
     * 查询当前用户购物车列表接口。
     *
     * @return 购物车商品列表
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> listShoppingCart() {
        return Result.success(shoppingCartService.list());
    }

    /**
     * 清空购物车接口。
     *
     * @return 清空成功提示
     */
    @DeleteMapping("/clean")
    public Result<Void> cleanShoppingCart() {
        shoppingCartService.clean();
        return Result.success();
    }

    /**
     * 减少购物车商品数量接口。
     * <p>
     * 当商品数量减至0时自动从购物车中移除。
     *
     * @param shoppingCartDTO 待减少的商品信息
     * @return 操作成功提示
     */
    @PostMapping("/sub")
    public Result<Void> subShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.editShoppingCart(shoppingCartDTO, false);
        return Result.success();
    }
}
