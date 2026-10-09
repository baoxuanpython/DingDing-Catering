package com.dingdingcatering.service.user.impl;

import com.dingdingcatering.constant.MessageConstant;
import com.dingdingcatering.context.BaseContext;
import com.dingdingcatering.entity.AddressBook;
import com.dingdingcatering.mapper.user.AddressBookMapper;
import com.dingdingcatering.service.user.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {
    private final AddressBookMapper addressBookMapper;

    public AddressBookServiceImpl(AddressBookMapper addressBookMapper) {
        this.addressBookMapper = addressBookMapper;
    }

    @Override
    public void save(AddressBook addressBook) {
        Long currentUserId = BaseContext.getCurrentId();
        validateUserIdOwnership(addressBook.getUserId(), currentUserId);
        addressBookMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> list() {
        return addressBookMapper.list(BaseContext.getCurrentId());
    }

    @Override
    public AddressBook defaultAddressBook() {
        return addressBookMapper.defaultAddressBook(BaseContext.getCurrentId());
    }

    @Override
    @Transactional
    public void updateDefaultAddressBook(Integer id) {
        Long userId = BaseContext.getCurrentId();
        validateUserIdOwnership((long) id, userId);
        addressBookMapper.clearDefaultByUserId(userId);
        addressBookMapper.updateDefaultAddressBook(id, userId);
    }

    @Override
    public void updateAddressBook(AddressBook addressBook) {
        Long currentUserId = BaseContext.getCurrentId();
        validateUserIdOwnership(addressBook.getUserId(), currentUserId);
        addressBookMapper.updateAddressBook(addressBook);
    }

    @Override
    public void deleteAddressBook(Long id) {
        addressBookMapper.delete(id, BaseContext.getCurrentId());
    }

    @Override
    public AddressBook getAddressBook(Long id) {
        return addressBookMapper.selectById(id, BaseContext.getCurrentId());
    }

    private void validateUserIdOwnership(Long requestUserId, Long currentUserId) {
        if (requestUserId != null && !Objects.equals(requestUserId, currentUserId)) {
            log.warn("用户身份校验失败: 请求userId={}, 当前登录userId={}", requestUserId, currentUserId);
            throw new SecurityException(MessageConstant.SECURITY_ERROR);
        }
    }
}
