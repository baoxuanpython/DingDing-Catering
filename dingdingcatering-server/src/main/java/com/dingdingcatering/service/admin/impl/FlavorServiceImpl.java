package com.dingdingcatering.service.admin.impl;

import com.dingdingcatering.dto.FlavorDTO;
import com.dingdingcatering.dto.FlavorPageQueryDTO;
import com.dingdingcatering.entity.Flavor;
import com.dingdingcatering.enumeration.ErrorCode;
import com.dingdingcatering.exception.FlavorException;
import com.dingdingcatering.mapper.admin.FlavorMapper;
import com.dingdingcatering.result.PageResult;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.admin.FlavorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlavorServiceImpl implements FlavorService {
    private final FlavorMapper flavorMapper;

    public FlavorServiceImpl(FlavorMapper flavorMapper) {
        this.flavorMapper = flavorMapper;
    }

    @Override
    public void addFlavor(FlavorDTO flavorDTO) {
        FlavorDTO existingFlavor = flavorMapper.getByName(flavorDTO.getFlavorName());
        if (existingFlavor != null) {
            throw new FlavorException(ErrorCode.FLAVOR_NAME_EXIST, existingFlavor.getFlavorName());
        }
        flavorMapper.insert(flavorDTO);
    }

    @Override
    public void updateFlavor(FlavorDTO flavorDTO) {
        flavorMapper.update(flavorDTO);
    }

    @Override
    public PageResult<FlavorDTO> pageQueryFlavor(FlavorPageQueryDTO flavorPageQueryDTO) {
        try (Page<FlavorDTO> page = PageHelper.startPage(flavorPageQueryDTO.getPage(), flavorPageQueryDTO.getPageSize())) {
            List<FlavorDTO> flavors = flavorMapper.pageQuery(flavorPageQueryDTO);
            return new PageResult<>(page.getTotal(), flavors);
        }
    }

    @Override
    public FlavorDTO getById(Long id) {
        return flavorMapper.getById(id);
    }

    @Override
    public List<FlavorDTO> listFlavor() {
        return flavorMapper.listFlavor();
    }

    @Override
    public void deleteFlavor(Long id) {
        List<String> dishNames = flavorMapper.getDishFlavorById(id);
        if (!dishNames.isEmpty()) {
            throw new FlavorException(ErrorCode.FLAVOR_DELETE_FAILED, String.join(",", dishNames));
        }
        flavorMapper.deleteById(id);
    }
}