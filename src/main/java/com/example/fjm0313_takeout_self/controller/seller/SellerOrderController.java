package com.example.fjm0313_takeout_self.controller.seller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.vo.OrdersVO;
import com.example.fjm0313_takeout_self.entity.OrderDetail;
import com.example.fjm0313_takeout_self.entity.Orders;
import com.example.fjm0313_takeout_self.service.OrderDetailService;
import com.example.fjm0313_takeout_self.service.OrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seller/orders")
public class SellerOrderController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;

    // 只放 sellerList 这个方法
    @GetMapping("/sellerList")
    public Result<List<OrdersVO>> sellerList(@RequestParam(required = false) Integer status ){
        List<Orders> ordersList = ordersService.findAll(status);

        List<OrdersVO> voList = ordersList.stream().map(order ->{
            OrdersVO vo = new OrdersVO();
            BeanUtils.copyProperties(order,vo);
            List<OrderDetail> detailList = orderDetailService.findByOrderId(order.getId());
            vo.setOrderDetails(detailList);
            return vo;

        }).toList();

        return Result.success(voList);

    }
}
