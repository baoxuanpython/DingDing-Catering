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
        log.info("新增地址: userId={}", currentUserId);
        validateUserIdOwnership(addressBook.getUserId(), currentUserId);
        addressBookMapper.insert(addressBook);
        log.info("新增地址成功: id={}, userId={}", addressBook.getId(), currentUserId);
    }

    @Override
    public List<AddressBook> list() {
        Long currentUserId = BaseContext.getCurrentId();
        log.debug("查询地址列表: userId={}", currentUserId);
        return addressBookMapper.list(currentUserId);
    }

    @Override
    public AddressBook defaultAddressBook() {
        Long currentUserId = BaseContext.getCurrentId();
        log.debug("查询默认地址: userId={}", currentUserId);
        return addressBookMapper.defaultAddressBook(currentUserId);
    }

    @Override
    @Transactional
    public void updateDefaultAddressBook(Integer id) {
        Long userId = BaseContext.getCurrentId();
        log.info("更新默认地址: id={}, userId={}", id, userId);
        validateUserIdOwnership((long) id, userId);
        addressBookMapper.clearDefaultByUserId(userId);
        addressBookMapper.updateDefaultAddressBook(id, userId);
        log.info("更新默认地址成功: id={}, userId={}", id, userId);
    }

    @Override
    public void updateAddressBook(AddressBook addressBook) {
        Long currentUserId = BaseContext.getCurrentId();
        log.info("更新地址: id={}, userId={}", addressBook.getId(), currentUserId);
        validateUserIdOwnership(addressBook.getUserId(), currentUserId);
        addressBookMapper.updateAddressBook(addressBook);
        log.info("更新地址成功: id={}", addressBook.getId());
    }

    @Override
    public void deleteAddressBook(Long id) {
        Long currentUserId = BaseContext.getCurrentId();
        log.info("删除地址: id={}, userId={}", id, currentUserId);
        addressBookMapper.delete(id, currentUserId);
        log.info("删除地址成功: id={}", id);
    }

    @Override
    public AddressBook getAddressBook(Long id) {
        Long currentUserId = BaseContext.getCurrentId();
        log.debug("查询地址详情: id={}, userId={}", id, currentUserId);
        return addressBookMapper.selectById(id, currentUserId);
    }

    private void validateUserIdOwnership(Long requestUserId, Long currentUserId) {
        if (requestUserId != null && !Objects.equals(requestUserId, currentUserId)) {
            log.warn("用户身份校验失败: 请求userId={}, 当前登录userId={}", requestUserId, currentUserId);
            throw new SecurityException(MessageConstant.SECURITY_ERROR);
        }
    }
}
