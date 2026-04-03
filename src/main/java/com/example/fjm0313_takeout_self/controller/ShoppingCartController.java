package com.example.fjm0313_takeout_self.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.entity.ShoppingCart;
import com.example.fjm0313_takeout_self.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ShoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;

    // 添加
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart cart, HttpServletRequest request){
        // 找出用户的订单，找有没有这道菜，有就把数量＋1，没有就新加
        Long userId = (Long) request.getSession().getAttribute("userId");
        cart.setUserId(userId);

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        wrapper.eq(ShoppingCart::getDishId,cart.getDishId());
        wrapper.eq(ShoppingCart::getFlavor,cart.getFlavor());
        wrapper.eq(ShoppingCart::getPortion,cart.getPortion());

        ShoppingCart existCart = cartService.getOne(wrapper);

        if(existCart != null){
            existCart.setNumber(existCart.getNumber() + 1);
            cartService.updateById(existCart);
            return Result.success(existCart);
        }else{
            cart.setNumber(1);
            cartService.save(cart);
            return Result.success(cart);
        }
    }

    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart cart , HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,id);
        wrapper.eq(ShoppingCart::getDishId,cart.getDishId());
        wrapper.eq(ShoppingCart::getFlavor,cart.getFlavor());
        wrapper.eq(ShoppingCart::getPortion,cart.getPortion());

        ShoppingCart existCart = cartService.getOne(wrapper);
        if(existCart == null){
            return Result.fail("购物单不存在");

        }
        int number = existCart.getNumber() - 1;

        if(number > 0){
            existCart.setNumber(number);
            cartService.updateById(existCart);
            return Result.success(existCart);
        }else{
            cartService.removeById(existCart);
            return Result.success(null);
        }


    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,id);
        wrapper.orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = cartService.list(wrapper);

        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result<String> clean(HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,id);

        cartService.remove(wrapper);

        return Result.success("清空成功");

    }
}
