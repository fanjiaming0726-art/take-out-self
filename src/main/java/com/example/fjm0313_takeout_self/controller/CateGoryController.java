package com.example.fjm0313_takeout_self.controller;

import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.entity.Category;
import com.example.fjm0313_takeout_self.service.CateGoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CateGoryController {


    @Autowired
    private CateGoryService cateGoryService;

    @PostMapping("/save")
    public Result<String> save(@RequestBody Category category){
        cateGoryService.save(category);
        return Result.success("保存成功");
    }


    @PostMapping("/update")
    public Result<String> update(@RequestBody Category category){
        cateGoryService.updateById(category);
        return Result.success("更新成功");
    }

    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id){
        Category category = cateGoryService.getById(id);
        return Result.success(category);
    }

    @GetMapping("/list")
    public Result<List<Category>> list(){
        List<Category> list = cateGoryService.list();
        return Result.success(list);
    }
}
