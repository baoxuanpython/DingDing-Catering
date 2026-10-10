package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    ShoppingCart findByUserIdAndDishId(@Param("userId") Long userId,
                                       @Param("dishId") Long dishId,
                                       @Param("dishFlavor") String dishFlavor);

    ShoppingCart findByUserIdAndSetmealId(@Param("userId") Long userId,
                                          @Param("setmealId") Long setmealId);

    void updateNumber(@Param("id") Long id,
                      @Param("number") Integer number);

    void insert(ShoppingCart shoppingCart);

    List<ShoppingCart> listByUserId(Long userId);

    void cleanByUserId(Long userId);

    void deleteOne(Long id);
}
