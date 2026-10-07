package com.dingdingcatering.controller.user;

import com.dingdingcatering.entity.AddressBook;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.AddressBookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {
    private final AddressBookService addressBookService;
    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }
    @PostMapping
    public Result<Void> save(@RequestBody AddressBook addressBook) {
        return Result.success(addressBookService.save(addressBook));
    }
}
