package com.dingdingcatering.service.user;

import com.dingdingcatering.dto.ShoppingCartDTO;
import com.dingdingcatering.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void editShoppingCart(ShoppingCartDTO shoppingCartDTO, boolean isAddOrSub);

    List<ShoppingCart> list();

    void clean();

}
