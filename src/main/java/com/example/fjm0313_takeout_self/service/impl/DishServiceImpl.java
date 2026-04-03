package com.example.fjm0313_takeout_self.service.impl;

import com.example.fjm0313_takeout_self.entity.Dish;
import com.example.fjm0313_takeout_self.mapper.DishMapper;
import com.example.fjm0313_takeout_self.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;


    @Override
    public void addDish(Dish dish) {
        dishMapper.insert(dish);
    }

    @Override
    public void deleteDishByIds(List<Long> ids) {
        dishMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Dish> findAll() {
        return dishMapper.selectList(null);
    }

    @Override
    public Dish findById(Long id) {
       return dishMapper.selectById(id);
    }

    @Override
    public void updateDish(Dish dish) {
        dishMapper.updateById(dish);
    }
}