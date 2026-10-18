package com.dingdingcatering.service.user;

import com.dingdingcatering.dto.ShoppingCartDTO;
import com.dingdingcatering.entity.ShoppingCart;

import java.util.List;

/**
 * C端购物车服务接口
 */
public interface ShoppingCartService {
    /**
     * 编辑购物车（添加/减少商品）。
     *
     * @param shoppingCartDTO 商品信息
     * @param isAddOrSub      true-添加，false-减少
     */
    void editShoppingCart(ShoppingCartDTO shoppingCartDTO, boolean isAddOrSub);

    /**
     * 查询当前用户购物车列表。
     *
     * @return 购物车商品列表
     */
    List<ShoppingCart> list();

    /**
     * 清空购物车。
     */
    void clean();

    /**
     * 修改购物车商品数量。
     *
     * @param id     购物车记录ID
     * @param number 目标数量（必须大于0）
     */
    void updateCartNumber(Long id, Integer number);

    /**
     * 删除购物车单个商品。
     *
     * @param id 购物车记录ID
     */
    void deleteCartItem(Long id);

}