package com.dingdingcatering.mapper.admin;

import com.dingdingcatering.annotation.AutoFill;
import com.dingdingcatering.enumeration.OperationType;
import com.dingdingcatering.dto.FlavorDTO;
import com.dingdingcatering.dto.FlavorPageQueryDTO;
import com.dingdingcatering.entity.Flavor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("adminFlavorMapper")
public interface FlavorMapper {

    FlavorDTO getByName(String flavorName);

    @AutoFill
    void insert(FlavorDTO flavorDTO);

    @AutoFill(OperationType.UPDATE)
    void update(FlavorDTO flavorDTO);

    List<FlavorDTO> pageQuery(FlavorPageQueryDTO flavorPageQueryDTO);

    FlavorDTO getById(Long id);

    List<FlavorDTO> listFlavor();

    List<String> getDishFlavorById(Long id);

    void deleteById(Long id);
}