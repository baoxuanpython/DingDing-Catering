package com.dingdingcatering.service.admin;

import com.dingdingcatering.dto.FlavorDTO;
import com.dingdingcatering.dto.FlavorPageQueryDTO;
import com.dingdingcatering.mapper.admin.FlavorMapper;
import com.dingdingcatering.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FlavorService {

    void addFlavor(FlavorDTO flavorDTO);

    void updateFlavor(FlavorDTO flavorDTO);

    PageResult<FlavorDTO> pageQueryFlavor(FlavorPageQueryDTO flavorPageQueryDTO);

    FlavorDTO getById(Long id);

    List<FlavorDTO> listFlavor();

    void deleteFlavor(Long id);
}
