package com.dingdingcatering.service.user;

import com.dingdingcatering.entity.AddressBook;

import java.util.List;

/**
 * C端地址簿服务接口
 */
public interface AddressBookService {
    /**
     * 新增地址。
     *
     * @param addressBook 地址信息
     */
    void save(AddressBook addressBook);

    /**
     * 查询当前用户地址簿列表。
     *
     * @return 地址簿列表
     */
    List<AddressBook> list();

    /**
     * 查询默认地址。
     *
     * @return 默认地址
     */
    AddressBook defaultAddressBook();

    /**
     * 更新地址信息。
     *
     * @param addressBook 待更新的地址信息
     */
    void updateAddressBook(AddressBook addressBook);

    /**
     * 删除地址。
     *
     * @param id 地址ID
     */
    void deleteAddressBook(Long id);

    /**
     * 根据ID查询地址。
     *
     * @param id 地址ID
     * @return 地址详情
     */
    AddressBook getAddressBook(Long id);

    /**
     * 设置默认地址。
     *
     * @param id 地址ID
     */
    void setDefaultAddressBook(Long id);
}
