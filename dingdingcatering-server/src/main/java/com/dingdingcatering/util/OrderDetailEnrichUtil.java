package com.dingdingcatering.util;

import com.dingdingcatering.entity.OrderDetail;
import com.dingdingcatering.entity.SetMealDish;
import com.dingdingcatering.entity.SetMealDishFlavor;
import com.dingdingcatering.mapper.admin.DishMapper;
import com.dingdingcatering.mapper.admin.SetMealMapper;
import com.dingdingcatering.vo.DishVO;
import com.dingdingcatering.vo.SetMealVO;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单明细填充工具类
 * 用于填充订单明细中的菜品/套餐完整信息，处理套餐聚合逻辑
 */
public class OrderDetailEnrichUtil {

    private OrderDetailEnrichUtil() {
    }

    /**
     * 填充订单明细中的菜品/套餐完整信息
     * 聚合逻辑：
     * - 单点菜品（setmealId == null）：保持原样，填充 dishVO
     * - 套餐菜品（setmealId != null）：按 setmealId 分组聚合，只保留一条代表套餐的记录，
     * 将套餐内所有菜品聚合到 setmealVO.setmealDishes 中（替换模板数据）
     *
     * @param orderDetailList 订单明细列表
     * @param dishMapper      菜品 Mapper
     * @param setMealMapper   套餐 Mapper
     */
    public static void enrichOrderDetails(List<OrderDetail> orderDetailList, DishMapper dishMapper, SetMealMapper setMealMapper) {
        if (CollectionUtils.isEmpty(orderDetailList)) {
            return;
        }

        // 分离单点菜品和套餐菜品
        List<OrderDetail> singleDishDetails = orderDetailList.stream()
                .filter(d -> d.getSetmealId() == null && d.getDishId() != null)
                .collect(Collectors.toList());

        Map<Long, List<OrderDetail>> setmealDetailsMap = orderDetailList.stream()
                .filter(d -> d.getSetmealId() != null)
                .collect(Collectors.groupingBy(OrderDetail::getSetmealId));

        // 批量查询单点菜品信息
        enrichSingleDishDetails(singleDishDetails, dishMapper);

        // 批量查询套餐信息并聚合
        if (!setmealDetailsMap.isEmpty()) {
            List<OrderDetail> aggregatedSetmealDetails = aggregateSetmealDetails(
                    setmealDetailsMap, dishMapper, setMealMapper);

            // 从原列表中移除所有套餐明细，添加聚合后的代表记录
            orderDetailList.removeIf(d -> d.getSetmealId() != null);
            orderDetailList.addAll(aggregatedSetmealDetails);
        }
    }

    /**
     * 填充单点菜品信息
     *
     * @param singleDishDetails 单点菜品明细列表
     * @param dishMapper        菜品 Mapper
     */
    public static void enrichSingleDishDetails(List<OrderDetail> singleDishDetails, DishMapper dishMapper) {
        if (CollectionUtils.isEmpty(singleDishDetails)) {
            return;
        }

        List<Long> dishIds = singleDishDetails.stream()
                .map(OrderDetail::getDishId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(dishIds)) {
            Map<Long, DishVO> dishMap = dishMapper.getByIds(dishIds).stream()
                    .collect(Collectors.toMap(DishVO::getId, Function.identity()));
            singleDishDetails.forEach(detail -> detail.setDishVO(dishMap.get(detail.getDishId())));
        }
    }

    /**
     * 处理套餐聚合逻辑
     * 按 setmealId 分组聚合套餐菜品，将套餐内所有菜品聚合到 setmealVO.setmealDishes 中
     *
     * @param setmealDetailsMap 套餐明细映射（key: setmealId, value: 该套餐的订单明细列表）
     * @param dishMapper        菜品 Mapper
     * @param setMealMapper     套餐 Mapper
     * @return 聚合后的套餐代表记录列表
     */
    public static List<OrderDetail> aggregateSetmealDetails(
            Map<Long, List<OrderDetail>> setmealDetailsMap,
            DishMapper dishMapper,
            SetMealMapper setMealMapper) {

        if (setmealDetailsMap.isEmpty()) {
            return Collections.emptyList();
        }

        // 批量查询套餐信息
        List<Long> setmealIds = new ArrayList<>(setmealDetailsMap.keySet());
        Map<Long, SetMealVO> setmealMap = setMealMapper.getByIds(setmealIds).stream()
                .collect(Collectors.toMap(SetMealVO::getId, Function.identity()));

        // 收集套餐内所有菜品的 dishId，批量查询
        List<Long> allSetmealDishIds = setmealDetailsMap.values().stream()
                .flatMap(List::stream)
                .map(OrderDetail::getDishId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, DishVO> allSetmealDishMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(allSetmealDishIds)) {
            allSetmealDishMap = dishMapper.getByIds(allSetmealDishIds).stream()
                    .collect(Collectors.toMap(DishVO::getId, Function.identity()));
        }

        // 对每个套餐进行聚合
        final Map<Long, DishVO> finalAllSetmealDishMap = allSetmealDishMap;
        List<OrderDetail> aggregatedSetmealDetails = new ArrayList<>();
        setmealDetailsMap.forEach((setmealId, details) -> {
            SetMealVO setmealVO = setmealMap.get(setmealId);
            if (setmealVO == null || details.isEmpty()) {
                return;
            }

            // 取第一条作为套餐代表记录
            OrderDetail representative = details.get(0);

            // 构建用户实际点的菜品列表（替换 setmealDishes 模板数据）
            List<SetMealDish> orderedDishes = details.stream().map(detail -> {
                DishVO dishVO = finalAllSetmealDishMap.get(detail.getDishId());
                SetMealDish setMealDish = new SetMealDish();
                setMealDish.setDishId(detail.getDishId());
                // 菜品名称从 DishVO 获取（order_detail.name 存的是套餐名，不是菜品名）
                setMealDish.setName(dishVO != null ? dishVO.getName() : detail.getName());
                // 菜品单价从 DishVO 获取（detail.amount 是套餐总价，不是菜品单价）
                setMealDish.setPrice(dishVO != null ? dishVO.getPrice() : detail.getAmount());
                setMealDish.setCopies(detail.getDishCopies());

                // 解析口味信息
                if (detail.getFlavorId() != null && !detail.getFlavorId().isEmpty()) {
                    List<SetMealDishFlavor> flavors = parseFlavors(detail);
                    setMealDish.setSetmealDishFlavors(flavors);
                }
                return setMealDish;
            }).collect(Collectors.toList());

            // 替换 setmealDishes 为用户实际点的菜品列表
            setmealVO.setSetmealDishes(orderedDishes);

            // 更新代表记录
            representative.setName(setmealVO.getName());
            representative.setDishId(null);
            representative.setSetmealVO(setmealVO);

            aggregatedSetmealDetails.add(representative);
        });

        return aggregatedSetmealDetails;
    }

    /**
     * 解析订单明细中的口味信息
     *
     * @param detail 订单明细
     * @return 口味列表
     */
    private static List<SetMealDishFlavor> parseFlavors(OrderDetail detail) {
        String[] flavorIds = detail.getFlavorId().split(",");
        String[] flavorNames = detail.getFlavorName() != null ? detail.getFlavorName().split(",") : new String[0];
        String[] flavorVals = detail.getFlavorVal() != null ? detail.getFlavorVal().split(",") : new String[0];

        List<SetMealDishFlavor> flavors = new ArrayList<>();
        for (int i = 0; i < flavorIds.length; i++) {
            SetMealDishFlavor flavor = new SetMealDishFlavor();
            flavor.setFlavorId(Long.parseLong(flavorIds[i].trim()));
            if (i < flavorNames.length) {
                flavor.setFlavorName(flavorNames[i].trim());
            }
            if (i < flavorVals.length) {
                flavor.setFlavorValue(flavorVals[i].trim());
            }
            flavors.add(flavor);
        }
        return flavors;
    }
}