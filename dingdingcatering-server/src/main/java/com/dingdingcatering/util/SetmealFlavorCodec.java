package com.dingdingcatering.util;

import com.dingdingcatering.dto.SetmealFlavorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐口味编码/解码工具类
 * <p>
 * 购物车表没有「套餐内每道菜的口味」这样的结构，只能复用 flavor_id / flavor_val 两列：
 * <ul>
 *     <li>flavor_id 存「菜品ID:口味类型ID」逗号分隔串，如 {@code "101:1,102:2"}</li>
 *     <li>flavor_val 存与 flavor_id 顺序对齐的口味值串，如 {@code "微辣,常温"}</li>
 * </ul>
 * 同一个菜品有多组口味类型时，该菜品ID会重复出现，如 {@code "101:1,101:2"}。
 * <p>
 * 编码前按「菜品ID、口味类型ID」升序归一化，保证同一口味组合永远得到同一个字符串，
 * 这样购物车按 flavor_id 查重才可靠。订单明细中的 flavor_id 必须是纯数字ID串，
 * 因此解码后的编码串只在购物车中使用，不能直接写入 order_detail。
 */
public class SetmealFlavorCodec {

    /** shopping_cart.flavor_id / flavor_val 列长度上限 */
    public static final int MAX_ENCODED_LENGTH = 255;

    private static final String ENTRY_SEPARATOR = ",";
    private static final String PAIR_SEPARATOR = ":";

    private SetmealFlavorCodec() {
    }

    /**
     * 编码结果：可直接写入 shopping_cart 的 flavor_id / flavor_val
     */
    @Getter
    @AllArgsConstructor
    public static class EncodedFlavors {
        private final String flavorId;
        private final String flavorVal;
    }

    /**
     * 解码后的单个口味选择（一道菜的一组口味）
     */
    @Getter
    @AllArgsConstructor
    public static class FlavorPair {
        private final Long dishId;
        private final Long flavorId;
        private final String flavorVal;
    }

    /**
     * 将前端提交的套餐口味编码为购物车存储串
     *
     * @param setmealFlavors 套餐内各菜品的口味选择，可为空
     * @return 编码结果，无口味时两个字段均为 null
     */
    public static EncodedFlavors encode(List<SetmealFlavorDTO> setmealFlavors) {
        if (CollectionUtils.isEmpty(setmealFlavors)) {
            return new EncodedFlavors(null, null);
        }

        List<SetmealFlavorDTO> normalized = new ArrayList<>(setmealFlavors);
        normalized.sort(Comparator.comparing(SetmealFlavorDTO::getDishId));

        List<FlavorPair> pairs = new ArrayList<>();
        for (SetmealFlavorDTO item : normalized) {
            if (CollectionUtils.isEmpty(item.getFlavorId()) || CollectionUtils.isEmpty(item.getFlavorVal())) {
                continue;
            }
            int size = Math.min(item.getFlavorId().size(), item.getFlavorVal().size());
            for (int i = 0; i < size; i++) {
                pairs.add(new FlavorPair(item.getDishId(), item.getFlavorId().get(i), item.getFlavorVal().get(i)));
            }
        }

        pairs.sort(Comparator.comparing(FlavorPair::getDishId).thenComparing(FlavorPair::getFlavorId));

        StringBuilder flavorIdBuilder = new StringBuilder();
        StringBuilder flavorValBuilder = new StringBuilder();
        for (FlavorPair pair : pairs) {
            if (!flavorIdBuilder.isEmpty()) {
                flavorIdBuilder.append(ENTRY_SEPARATOR);
                flavorValBuilder.append(ENTRY_SEPARATOR);
            }
            flavorIdBuilder.append(pair.getDishId()).append(PAIR_SEPARATOR).append(pair.getFlavorId());
            flavorValBuilder.append(pair.getFlavorVal());
        }

        if (flavorIdBuilder.isEmpty()) {
            return new EncodedFlavors(null, null);
        }
        return new EncodedFlavors(flavorIdBuilder.toString(), flavorValBuilder.toString());
    }

    /**
     * 解码购物车中的套餐口味编码串
     *
     * @param flavorId  形如 "101:1,102:2" 的编码串
     * @param flavorVal 形如 "微辣,常温" 的口味值串
     * @return key 为菜品ID，value 为该菜品按顺序排列的口味选择
     */
    public static Map<Long, List<FlavorPair>> decode(String flavorId, String flavorVal) {
        Map<Long, List<FlavorPair>> result = new LinkedHashMap<>();
        if (flavorId == null || flavorId.isBlank()) {
            return result;
        }

        String[] entries = flavorId.split(ENTRY_SEPARATOR);
        String[] values = flavorVal == null ? new String[0] : flavorVal.split(ENTRY_SEPARATOR);

        for (int i = 0; i < entries.length; i++) {
            String entry = entries[i].trim();
            if (entry.isEmpty()) {
                continue;
            }
            int separatorIndex = entry.indexOf(PAIR_SEPARATOR);
            if (separatorIndex <= 0 || separatorIndex == entry.length() - 1) {
                continue;
            }
            Long dishId = Long.valueOf(entry.substring(0, separatorIndex).trim());
            Long flavorTypeId = Long.valueOf(entry.substring(separatorIndex + 1).trim());
            String value = i < values.length ? values[i].trim() : null;
            result.computeIfAbsent(dishId, key -> new ArrayList<>()).add(new FlavorPair(dishId, flavorTypeId, value));
        }
        return result;
    }

}