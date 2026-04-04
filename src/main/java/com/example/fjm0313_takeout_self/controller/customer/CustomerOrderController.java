package com.example.fjm0313_takeout_self.controller.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.common.LoginRequired;
import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.common.UserContext;
import com.example.fjm0313_takeout_self.vo.OrdersVO;
import com.example.fjm0313_takeout_self.entity.*;
import com.example.fjm0313_takeout_self.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("customer/orders")
public class CustomerOrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private AddressBookService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @LoginRequired("CUSTOMER")
    @PostMapping("/submit")
    public Result<Orders> submit(@RequestBody Orders orders){
        // 查有无购物车
        Long userId = UserContext.getUserId();
        List<ShoppingCart> cartList = cartService.findByUserId(userId);

        if(cartList == null || cartList.isEmpty()){
            return Result.fail("用户未下单");
        }

        // 查有无地址
        LambdaQueryWrapper<AddressBook> addressWrapper = new LambdaQueryWrapper<>();
        addressWrapper.eq(AddressBook::getUserId,userId);

        AddressBook addressBook = addressService.findById(orders.getAddressBookId());
        if(addressBook == null){
            return Result.fail("地址信息有误，请重新填写");
        }

        // 查用户
        User user = userService.findById(userId);

        // 生成订单号
        String orderNumber = UUID.randomUUID().toString().replace("-","");


        // 计算金额
        BigDecimal amountAll = BigDecimal.ZERO ;
        for(ShoppingCart cart : cartList){
            amountAll = amountAll.add(cart.getAmount().multiply(new BigDecimal(cart.getNumber())));
        }
        orders.setNumber(orderNumber);
        orders.setStatus(1);
        orders.setConsignee(addressBook.getConsignee());
        orders.setUsername(user.getUsername());
        orders.setUserId(userId);
        orders.setAmount(amountAll);
        String fullAddress = (addressBook.getProvinceName()== null ? "" : addressBook.getProvinceName())
                        + (addressBook.getCityName()== null ? "" : addressBook.getCityName())
                        + (addressBook.getDistrictName()== null ? "" : addressBook.getDistrictName())
                        + (addressBook.getDetail()== null ? "" : addressBook.getDetail())
        ;
        orders.setAddress(fullAddress);

        ordersService.createOrder(orders);

        // 8. 保存订单明细
        List<OrderDetail> details = cartList.stream().map( cart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            orderDetail.setName(cart.getName());
            orderDetail.setImage(cart.getImage());
            orderDetail.setDishId(cart.getDishId());
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setAmount(cart.getAmount());
            orderDetail.setFlavor(cart.getFlavor());
            orderDetail.setPortion(cart.getPortion());
            return orderDetail;
        }).toList();
        orderDetailService.saveOrderDetails(details);

        cartService.clearByUserId(userId);

        return Result.success(orders);
    }

    @LoginRequired("CUSTOMER")
    @GetMapping("/userOrderList")
    public Result<List<OrdersVO>> userOrdersList(){
        Long userId = UserContext.getUserId();

        List<Orders> ordersList = ordersService.findByUserId(userId);
        List<OrdersVO> ordersVOList = ordersList.stream().map(order -> {
            OrdersVO ordersVO = new OrdersVO();
            BeanUtils.copyProperties(order,ordersVO);
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(order.getId());
            ordersVO.setOrderDetails(orderDetails);
            return ordersVO;
        }).toList();

        return Result.success(ordersVOList);
    }


    

}
