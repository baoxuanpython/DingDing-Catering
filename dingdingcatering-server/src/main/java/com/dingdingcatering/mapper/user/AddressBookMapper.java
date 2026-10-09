package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    void insert(AddressBook addressBook);

    List<AddressBook> list(Long userId);

    AddressBook defaultAddressBook(Long userId);

    void updateAddressBook(AddressBook addressBook);

    void delete(Long id, Long userId);

    AddressBook selectById(Long id, Long userId);

    void updateDefaultAddressBook(Integer id, Long userId);

    void clearDefaultByUserId(Long userId);
}
