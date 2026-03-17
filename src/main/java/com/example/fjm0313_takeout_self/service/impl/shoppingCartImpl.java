package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fjm0313_takeout_self.entity.shoppingCart;
import com.example.fjm0313_takeout_self.mapper.shoppingCartMapper;
import com.example.fjm0313_takeout_self.service.shoppingCartService;
import org.springframework.stereotype.Service;


@Service
public class shoppingCartImpl extends ServiceImpl<shoppingCartMapper, shoppingCart> implements shoppingCartService {
}
