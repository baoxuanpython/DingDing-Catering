package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.context.BaseContext;
import com.dingdingcatering.dto.SetmealFlavorDTO;
import com.dingdingcatering.dto.ShoppingCartDTO;
import com.dingdingcatering.entity.Dish;
import com.dingdingcatering.entity.SetMealDish;
import com.dingdingcatering.entity.Setmeal;
import com.dingdingcatering.entity.ShoppingCart;
import com.dingdingcatering.exception.ShoppingCartBusinessException;
import com.dingdingcatering.mapper.user.DishMapper;
import com.dingdingcatering.mapper.user.SetMealMapper;
import com.dingdingcatering.mapper.user.ShoppingCartMapper;
import com.dingdingcatering.service.user.ShoppingCartService;
import com.dingdingcatering.util.SetmealFlavorCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
            throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        boolean isDish = dishId != null;

        String flavorId;
        String flavorVal;
        if (isDish) {
            // 校验菜品口味参数：如果传了 flavorId，必须同时传 flavorVal，且长度一致
            if (!CollectionUtils.isEmpty(shoppingCartDTO.getFlavorId()) || !CollectionUtils.isEmpty(shoppingCartDTO.getFlavorVal())) {
                if (CollectionUtils.isEmpty(shoppingCartDTO.getFlavorId()) || CollectionUtils.isEmpty(shoppingCartDTO.getFlavorVal())) {
                    throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "flavorId 和 flavorVal 必须同时传递");
                }
                if (shoppingCartDTO.getFlavorId().size() != shoppingCartDTO.getFlavorVal().size()) {
                    throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "flavorId 和 flavorVal 的数量必须一致");
                }
            }
            // 将前端传来的数组拼接为逗号分隔的字符串
            flavorId = CollectionUtils.isEmpty(shoppingCartDTO.getFlavorId()) ? null :
                    shoppingCartDTO.getFlavorId().stream().map(String::valueOf).collect(Collectors.joining(","));
            flavorVal = CollectionUtils.isEmpty(shoppingCartDTO.getFlavorVal()) ? null :
                    String.join(",", shoppingCartDTO.getFlavorVal());
        } else {
            validateSetmealFlavors(shoppingCartDTO);
            SetmealFlavorCodec.EncodedFlavors encodedFlavors = SetmealFlavorCodec.encode(shoppingCartDTO.getSetmealFlavors());
            flavorId = encodedFlavors.getFlavorId();
            flavorVal = encodedFlavors.getFlavorVal();
            if (flavorId != null && (flavorId.length() > SetmealFlavorCodec.MAX_ENCODED_LENGTH
                    || flavorVal.length() > SetmealFlavorCodec.MAX_ENCODED_LENGTH)) {
                throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "套餐口味过多，无法加入购物车");
            }
        }

        ShoppingCart shoppingCart;
        if (isDish) {
            shoppingCart = shoppingCartMapper.findByUserIdAndDishId(userId, dishId, flavorId);
        } else {
            shoppingCart = shoppingCartMapper.findByUserIdAndSetmealId(userId, setmealId, flavorId);
        }
        if (!isAddOrSub) {
            if (shoppingCart != null) {
                if (shoppingCart.getNumber() == 1) {
                    shoppingCartMapper.deleteOne(shoppingCart.getId());
                } else {
                    shoppingCartMapper.updateNumber(shoppingCart.getId(), shoppingCart.getNumber() - 1);
                }
                return;
            } else {
                throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "购物车中不存在该商品");
            }
        }

        if (shoppingCart != null) {
            int newNumber = shoppingCart.getNumber() + 1;
            shoppingCartMapper.updateNumber(shoppingCart.getId(), newNumber);
        } else {
            if (isDish) {
                Dish dish = dishMapper.getById(dishId);
                if (dish == null) {
                    throw new ShoppingCartBusinessException(ErrorCode.DISH_IS_NULL);
                }
                if (dish.getStatus() != 1) {
                    throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "菜品未启售，不能加入购物车");
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
                    throw new ShoppingCartBusinessException(ErrorCode.SETMEAL_IS_NULL);
                }
                if (setmeal.getStatus() != 1) {
                    throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "套餐未启售，不能加入购物车");
                }
                shoppingCart = new ShoppingCart();
                shoppingCart.setUserId(userId);
                shoppingCart.setSetmealId(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }

            shoppingCart.setNumber(1);
            shoppingCart.setFlavorId(flavorId);
            shoppingCart.setFlavorVal(flavorVal);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 校验套餐口味参数：菜品必须属于该套餐，口味类型必须属于该套餐菜品
     */
    private void validateSetmealFlavors(ShoppingCartDTO shoppingCartDTO) {
        List<SetmealFlavorDTO> setmealFlavors = shoppingCartDTO.getSetmealFlavors();
        if (CollectionUtils.isEmpty(setmealFlavors)) {
            return;
        }

        for (SetmealFlavorDTO item : setmealFlavors) {
            if (item.getDishId() == null) {
                throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "套餐菜品ID不能为空");
            }
            if (CollectionUtils.isEmpty(item.getFlavorId()) || CollectionUtils.isEmpty(item.getFlavorVal())) {
                throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "套餐菜品的 flavorId 和 flavorVal 必须同时传递");
            }
            if (item.getFlavorId().size() != item.getFlavorVal().size()) {
                throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "套餐菜品的 flavorId 和 flavorVal 的数量必须一致");
            }
            if (item.getFlavorVal().stream().anyMatch(value -> value == null || value.isBlank())) {
                throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "套餐菜品的口味值不能为空");
            }
        }

        List<SetMealDish> setmealDishes = setMealMapper.queryDishesBySetmealIds(List.of(shoppingCartDTO.getSetmealId()));
        if (CollectionUtils.isEmpty(setmealDishes)) {
            throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "套餐内菜品数据异常，无法加入购物车");
        }

        Map<Long, SetMealDish> setmealDishMap = new HashMap<>();
        for (SetMealDish setmealDish : setmealDishes) {
            setmealDishMap.putIfAbsent(setmealDish.getDishId(), setmealDish);
        }

        for (SetmealFlavorDTO item : setmealFlavors) {
            SetMealDish setmealDish = setmealDishMap.get(item.getDishId());
            if (setmealDish == null) {
                throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "菜品不属于该套餐");
            }
            Set<Long> allowedFlavorIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(setmealDish.getSetmealDishFlavors())) {
                setmealDish.getSetmealDishFlavors().forEach(flavor -> allowedFlavorIds.add(flavor.getFlavorId()));
            }
            for (Long flavorTypeId : item.getFlavorId()) {
                if (!allowedFlavorIds.contains(flavorTypeId)) {
                    throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_ERROR, "口味类型不属于该套餐菜品");
                }
            }
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCartNumber(Long id, Integer number) {
        if (number == null || number <= 0) {
            throw new ShoppingCartBusinessException(ErrorCode.PARAM_ERROR, "数量必须大于0");
        }
        Long userId = BaseContext.getCurrentId();
        ShoppingCart cart = shoppingCartMapper.findById(id);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_IS_NULL);
        }
        shoppingCartMapper.updateNumber(id, number);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCartItem(Long id) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart cart = shoppingCartMapper.findById(id);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new ShoppingCartBusinessException(ErrorCode.SHOPPING_CART_IS_NULL);
        }
        shoppingCartMapper.deleteOne(id);
    }

}