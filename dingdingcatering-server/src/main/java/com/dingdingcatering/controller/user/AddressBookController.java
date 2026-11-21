package com.dingdingcatering.controller.user;

import com.dingdingcatering.dto.AddressBookDTO;
import com.dingdingcatering.dto.SetDefaultDTO;
import com.dingdingcatering.entity.AddressBook;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端地址簿控制器
 */
@RestController
@Slf4j
@RequestMapping("/user/addressBook")
public class AddressBookController {
    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    /**
     * 新增地址接口。
     *
     * @param dto 地址信息
     * @return 保存后的完整地址信息
     */
    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBookDTO dto) {
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(dto, addressBook);
        return Result.success(addressBookService.save(addressBook));
    }

    /**
     * 查询当前用户地址簿列表接口。
     *
     * @return 地址簿列表
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        return Result.success(addressBookService.list());
    }

    /**
     * 查询默认地址接口。
     *
     * @return 默认地址信息
     */
    @GetMapping("/default")
    public Result<AddressBook> defaultAddressBook() {
        return Result.success(addressBookService.defaultAddressBook());
    }

    /**
     * 更新地址信息接口。
     *
     * @param dto 待更新的地址信息
     * @return 更新后的完整地址信息
     */
    @PutMapping
    public Result<AddressBook> updateAddressBook(@RequestBody AddressBookDTO dto) {
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(dto, addressBook);
        return Result.success(addressBookService.updateAddressBook(addressBook));
    }

    /**
     * 删除地址接口。
     *
     * @param id 用于定位目标地址的主键标识
     * @return 删除成功提示
     */
    @DeleteMapping
    public Result<Void> deleteAddressBook(@RequestParam Long id) {
        addressBookService.deleteAddressBook(id);
        return Result.success();
    }

    /**
     * 根据ID查询地址详情接口。
     *
     * @param id 用于查询地址的唯一标识
     * @return 地址详情
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBook(@PathVariable Long id) {
        return Result.success(addressBookService.getAddressBook(id));
    }

    /**
     * 设置默认地址接口。
     *
     * @param dto 含目标地址ID
     * @return 设置成功提示
     */
    @PutMapping("/default")
    public Result<Void> setDefaultAddressBook(@RequestBody SetDefaultDTO dto) {
        addressBookService.setDefaultAddressBook(dto.getId());
        return Result.success();
    }
}