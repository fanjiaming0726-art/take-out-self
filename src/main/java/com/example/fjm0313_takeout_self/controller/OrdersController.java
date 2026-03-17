package com.example.fjm0313_takeout_self.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.dto.OrdersDto;
import com.example.fjm0313_takeout_self.entity.*;
import com.example.fjm0313_takeout_self.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private shoppingCartService cartService;

    @Autowired
    private AddressBookService addressService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public Result<Orders> submit(@RequestBody Orders orders, HttpServletRequest request){
        // 查有无购物车
        Long id = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<shoppingCart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(shoppingCart::getUserId,id);
        List<shoppingCart> cartList = cartService.list(cartWrapper);

        if(cartList == null || cartList.isEmpty()){
            return Result.fail("用户未下单");
        }

        // 查有无地址
        LambdaQueryWrapper<AddressBook> addressWrapper = new LambdaQueryWrapper<>();
        addressWrapper.eq(AddressBook::getUserId,id);

        AddressBook addressBook = addressService.getById(orders.getAddressBookId());
        if(addressBook == null){
            return Result.fail("地址信息有误，请重新填写");
        }

        // 查用户
        User user = userService.getById("userId");

        // 生成订单号
        String orderNumber = UUID.randomUUID().toString().replace("-","");


        // 计算金额
        BigDecimal amountAll = BigDecimal.ZERO ;
        for(shoppingCart cart : cartList){
            amountAll = amountAll.add(cart.getAmount().multiply(new BigDecimal(cart.getNumber())));
        }
        orders.setNumber(orderNumber);
        orders.setStatus(1);
        orders.setConsignee(addressBook.getConsignee());
        orders.setUsername(user.getUsername());
        orders.setUserId(id);
        orders.setAmount(amountAll);
        String fullAddress = (addressBook.getProvinceName()== null ? "" : addressBook.getProvinceName())
                        + (addressBook.getCityName()== null ? "" : addressBook.getCityName())
                        + (addressBook.getDistrictName()== null ? "" : addressBook.getDistrictName())
                        + (addressBook.getDetail()== null ? "" : addressBook.getDetail())
        ;
        orders.setAddress(fullAddress);

        ordersService.save(orders);

        // 8. 保存订单明细
        List<OrderDetail> details = cartList.stream().map( cart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            orderDetail.setName(cart.getName());
            orderDetail.setImage(cart.getImage());
            orderDetail.setDishId(cart.getDishId());
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setAmount(cart.getAmount());
            return orderDetail;
        }).toList();
        orderDetailService.saveBatch(details);

        cartService.remove(cartWrapper);

        return Result.success(orders);
    }

    @GetMapping("/userOrderList")
    public Result<List<OrdersDto>> userOrdersList(HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<Orders> ordersWrapper = new LambdaQueryWrapper<>();
        ordersWrapper.eq(Orders::getUserId,id);
        ordersWrapper.orderByDesc(Orders::getCreateTime);

        List<Orders> ordersList = ordersService.list(ordersWrapper);
        List<OrdersDto> ordersDtoList = ordersList.stream().map( order -> {
            OrdersDto orderDto = new OrdersDto();
            BeanUtils.copyProperties(order,orderDto);
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId,order.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
            orderDto.setOrderDetails(orderDetails);
            return orderDto;
        }).toList();

        return Result.success(ordersDtoList);
    }

    @GetMapping("/sellerList")
    public Result<List<OrdersDto>> sellerList(@RequestParam(required = false) Integer status ){
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        if(status != null){
            queryWrapper.eq(Orders::getStatus,status);
        }
        queryWrapper.orderByDesc(Orders::getCreateTime);
        List<Orders> ordersList = ordersService.list(queryWrapper);

        List<OrdersDto> dtoList = ordersList.stream().map(order ->{
            OrdersDto dto = new OrdersDto();
            BeanUtils.copyProperties(order,dto);
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId,order.getId());
            List<OrderDetail> detailList = orderDetailService.list(wrapper);
            dto.setOrderDetails(detailList);
            return dto;

        }).toList();

        return Result.success(dtoList);

    }


}
