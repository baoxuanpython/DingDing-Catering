package com.dingdingcatering.service.user;

import com.dingdingcatering.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void save(AddressBook addressBook);

    List<AddressBook> list();

    AddressBook defaultAddressBook();

    void updateAddressBook(AddressBook addressBook);

    void deleteAddressBook(Long id);

    AddressBook getAddressBook(Long id);

    void setDefaultAddressBook(Long id);
}
