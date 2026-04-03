package com.example.fjm0313_takeout_self.controller;


import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.entity.Dish;
import com.example.fjm0313_takeout_self.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping("/save")
    public Result<String> save(@RequestBody Dish dish){
        dishService.save(dish);
        return Result.success("保存成功");
    }

    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam List<Long> ids){
        dishService.removeByIds(ids);
        return Result.success("删除成功");
    }


    @GetMapping("/list")
    public Result<List<Dish>> list(){
        List<Dish> dishes = dishService.list();
        return Result.success(dishes);
    }

    @GetMapping("/{id}")
    public Result<Dish> getById(@PathVariable Long id){
        Dish dish = dishService.getById(id);
        return Result.success(dish);
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody Dish dish){
        dishService.updateById(dish);
        return Result.success("修改成功");
    }

}

