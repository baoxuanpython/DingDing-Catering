package com.dingdingcatering.controller.user;

import com.dingdingcatering.dto.ShoppingCartDTO;
import com.dingdingcatering.entity.ShoppingCart;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
    @PostMapping("/add")
    public Result<Void> addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.editShoppingCart(shoppingCartDTO, true);
        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<ShoppingCart>> listShoppingCart() {
        return Result.success(shoppingCartService.list());
    }
    @DeleteMapping("/clean")
    public Result<Void> cleanShoppingCart() {
        shoppingCartService.clean();
        return Result.success();
    }
    @PostMapping("/sub")
    public Result<Void> subShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.editShoppingCart(shoppingCartDTO, false);
        return Result.success();
    }
}
