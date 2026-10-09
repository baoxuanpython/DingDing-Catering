package com.dingdingcatering.controller.user;

import com.dingdingcatering.entity.AddressBook;
import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.user.AddressBookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {
    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @PostMapping
    public Result<Void> save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        return Result.success(addressBookService.list());
    }

    @GetMapping("/default")
    public Result<AddressBook> defaultAddressBook() {
        return Result.success(addressBookService.defaultAddressBook());
    }

    @PutMapping
    public Result<Void> updateAddressBook(@RequestBody AddressBook addressBook) {
        addressBookService.updateAddressBook(addressBook);
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> deleteAddressBook(@RequestParam Long id) {
        addressBookService.deleteAddressBook(id);
        return Result.success();
    }
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBook(@PathVariable Long id) {
        return Result.success(addressBookService.getAddressBook(id));
    }
    @PutMapping("/default")
    public Result<Void> updateDefaultAddressBook(@RequestParam Integer id) {
        addressBookService.updateDefaultAddressBook(id);
        return Result.success();
    }
}
