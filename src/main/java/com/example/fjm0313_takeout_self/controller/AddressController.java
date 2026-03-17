package com.example.fjm0313_takeout_self.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.entity.AddressBook;
import com.example.fjm0313_takeout_self.service.AddressBookService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressBookService addressBookService;


    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook, HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        if(id == null){
            return Result.fail("账号未登录");
        }
        addressBook.setUserId(id);
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook,HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        if(id == null){
            return Result.fail("账号未登录");
        }
        addressBookService.updateById(addressBook);
        return Result.success("修改成功");

    }

    @DeleteMapping
    public Result<String> delete(@RequestBody AddressBook addressBook,HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        if(id == null){
            return Result.fail("账号未登录");
        }
        addressBookService.removeById(addressBook);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<List<AddressBook>> list(HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        if(id == null){
            return Result.fail("账号未登录");
        }
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,id);
        queryWrapper.orderByDesc(AddressBook::getIsDefault).orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(queryWrapper);

        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id,HttpServletRequest request){
        Long checkId = (Long) request.getSession().getAttribute("userId");
        if (checkId == null){
            return Result.fail("用户未登录");
        }
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }


    @PutMapping("/default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook,HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        if (id == null){
            return Result.fail("用户未登录");
        }
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId,id);
        wrapper.set(AddressBook::getIsDefault,0);

        addressBookService.update(wrapper);


        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return Result.success(addressBook);
    }

    @GetMapping("/default")
    public Result<AddressBook> getDefault(HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        if (id == null){
            return Result.fail("用户未登录");
        }
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,id);
        wrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(wrapper);
        if(addressBook == null){
            return Result.fail("没有默认地址");
        }
        return Result.success(addressBook);
    }

}
