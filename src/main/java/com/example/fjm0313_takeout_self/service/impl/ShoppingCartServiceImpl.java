package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fjm0313_takeout_self.entity.ShoppingCart;
import com.example.fjm0313_takeout_self.mapper.ShoppingCartMapper;
import com.example.fjm0313_takeout_self.service.ShoppingCartService;
import org.springframework.stereotype.Service;


@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
