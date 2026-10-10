package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.context.BaseContext;
import com.dingdingcatering.dto.ShoppingCartDTO;
import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.entity.ShoppingCart;
import com.dingdingcatering.exception.ShoppingCartBusinessException;
import com.dingdingcatering.mapper.user.DishMapper;
import com.dingdingcatering.mapper.user.SetMealMapper;
import com.dingdingcatering.mapper.user.ShoppingCartMapper;
import com.dingdingcatering.service.user.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final DishMapper dishMapper;
    private final SetMealMapper setMealMapper;

    public ShoppingCartServiceImpl(ShoppingCartMapper shoppingCartMapper,
                                   DishMapper dishMapper,
                                   SetMealMapper setMealMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
        this.dishMapper = dishMapper;
        this.setMealMapper = setMealMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editShoppingCart(ShoppingCartDTO shoppingCartDTO, boolean isAddOrSub) {
        if (shoppingCartDTO.getDishId() == null && shoppingCartDTO.getSetmealId() == null) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        String dishFlavor = shoppingCartDTO.getDishFlavor();
        boolean isDish = dishId != null;
        ShoppingCart shoppingCart;

        if (dishId != null) {
            shoppingCart = shoppingCartMapper.findByUserIdAndDishId(userId, dishId, dishFlavor);
        } else {
            shoppingCart = shoppingCartMapper.findByUserIdAndSetmealId(userId, setmealId);
        }
        if(!isAddOrSub){
            if(shoppingCart != null){
                if(shoppingCart.getNumber() == 1){
                    shoppingCartMapper.deleteOne(shoppingCart.getId());
                }else {
                    shoppingCartMapper.updateNumber(shoppingCart.getId(), shoppingCart.getNumber() - 1);
                }
                return;
            }else {
                throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
            }
        }

        if (shoppingCart != null) {
            int newNumber = shoppingCart.getNumber() + 1;
            shoppingCartMapper.updateNumber(shoppingCart.getId(), newNumber);
        } else {
            if (isDish) {
                Dish dish = dishMapper.getById(dishId);
                if (dish == null) {
                    throw new ShoppingCartBusinessException(MessageConstant.DISH_IS_NULL);
                }
                shoppingCart = new ShoppingCart();
                shoppingCart.setUserId(userId);
                shoppingCart.setDishId(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            } else {
                Setmeal setmeal = setMealMapper.getById(setmealId);
                if (setmeal == null) {
                    throw new ShoppingCartBusinessException(MessageConstant.SETMEAL_IS_NULL);
                }
                shoppingCart = new ShoppingCart();
                shoppingCart.setUserId(userId);
                shoppingCart.setSetmealId(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }

            shoppingCart.setNumber(1);
            shoppingCart.setDishFlavor(dishFlavor);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        return shoppingCartMapper.listByUserId(userId);
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.cleanByUserId(userId);
    }

}
