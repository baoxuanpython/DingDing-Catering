package com.dingdingcatering.service.user.Impl;

import com.dingdingcatering.entity.AddressBook;
import com.dingdingcatering.mapper.user.AddressBookMapper;
import com.dingdingcatering.service.user.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {
    private final AddressBookMapper addressBookMapper;
    public AddressBookServiceImpl(AddressBookMapper addressBookMapper) {
        this.addressBookMapper = addressBookMapper;
    }

    @Override
    public void save(AddressBook addressBook) {
        addressBookMapper.insert(addressBook);
    }
}
