package com.dingdingcatering.mapper.user;

import com.dingdingcatering.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper {
    void insert(AddressBook addressBook);
}
