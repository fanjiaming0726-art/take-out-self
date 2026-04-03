package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fjm0313_takeout_self.entity.Dish;
import com.example.fjm0313_takeout_self.mapper.DishMapper;
import com.example.fjm0313_takeout_self.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
