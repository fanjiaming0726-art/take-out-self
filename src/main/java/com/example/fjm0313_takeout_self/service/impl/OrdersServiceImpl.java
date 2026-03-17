package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fjm0313_takeout_self.entity.Orders;
import com.example.fjm0313_takeout_self.mapper.OrdersMapper;
import com.example.fjm0313_takeout_self.service.OrdersService;
import org.springframework.stereotype.Service;


@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
}
