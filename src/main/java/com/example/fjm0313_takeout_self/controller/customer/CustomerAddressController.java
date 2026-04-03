package com.example.fjm0313_takeout_self.controller.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.entity.AddressBook;
import com.example.fjm0313_takeout_self.service.AddressBookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/address")
public class CustomerAddressController {

    @Autowired
    private AddressBookService addressBookService;


    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook, HttpServletRequest request){
        Long userId = getUserId(request);
        if(userId == null){
            return Result.fail("账号未登录");
        }
        addressBook.setUserId(userId);
        addressBookService.addAddress(addressBook);
        return Result.success(addressBook);
    }

    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook,HttpServletRequest request){
        Long userId = getUserId(request);
        if(userId == null){
            return Result.fail("账号未登录");
        }
        addressBookService.updateAddress(addressBook);
        return Result.success("修改成功");

    }

    @DeleteMapping
    public Result<String> delete(@RequestBody AddressBook addressBook,HttpServletRequest request){
        Long userId = getUserId(request);
        if(userId == null){
            return Result.fail("账号未登录");
        }
        addressBookService.deleteAddress(addressBook.getId());
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<List<AddressBook>> list(HttpServletRequest request){
        Long userId = getUserId(request);
        if(userId == null){
            return Result.fail("账号未登录");
        }

        List<AddressBook> list = addressBookService.findByUserId(userId);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id,HttpServletRequest request){
        Long userId = getUserId(request);
        if (userId == null){
            return Result.fail("用户未登录");
        }
        AddressBook addressBook = addressBookService.findById(id);
        return Result.success(addressBook);
    }


    @PutMapping("/default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook,HttpServletRequest request){
        Long userId = getUserId(request);
        if (userId == null){
            return Result.fail("用户未登录");
        }

        addressBookService.clearDefaultByUserId(userId);

        addressBook.setIsDefault(1);
        addressBookService.updateAddress(addressBook);

        return Result.success(addressBook);
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefault(HttpServletRequest request){
        Long userId = getUserId(request);
        if (userId == null){
            return Result.fail("用户未登录");
        }

        AddressBook addressBook = addressBookService.findDefaultByUserId(userId);
        if(addressBook == null){
            return Result.fail("没有默认地址");
        }
        return Result.success(addressBook);
    }


    private Long getUserId(HttpServletRequest request){
        return (Long) request.getSession().getAttribute("userId");
    }

}
