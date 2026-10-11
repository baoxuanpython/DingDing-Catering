package com.dingdingcatering.controller.user;

import com.dingdingcatering.entity.AddressBook;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.AddressBookService;
import lombok.extern.slf4j.Slf4j;
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
     * @param addressBook 地址信息
     * @return 添加成功提示
     */
    @PostMapping
    public Result<Void> save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
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
     * @param addressBook 待更新的地址信息
     * @return 更新成功提示
     */
    @PutMapping
    public Result<Void> updateAddressBook(@RequestBody AddressBook addressBook) {
        addressBookService.updateAddressBook(addressBook);
        return Result.success();
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
     * @param addressBook 含目标地址ID
     * @return 设置成功提示
     */
    @PutMapping("/default")
    public Result<Void> setDefaultAddressBook(@RequestBody AddressBook addressBook) {
        addressBookService.setDefaultAddressBook(addressBook.getId());
        return Result.success();
    }
}
