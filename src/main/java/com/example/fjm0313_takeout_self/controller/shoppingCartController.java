package com.example.fjm0313_takeout_self.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.entity.shoppingCart;
import com.example.fjm0313_takeout_self.service.shoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class shoppingCartController {

    @Autowired
    private shoppingCartService cartService;

    // 添加
    @PostMapping("/add")
    public Result<shoppingCart> add(@RequestBody shoppingCart cart, HttpServletRequest request){
        // 找出用户的订单，找有没有这道菜，有就把数量＋1，没有就新加
        Long id = (Long) request.getSession().getAttribute("userId");
        cart.setUserId(id);

        LambdaQueryWrapper<shoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(shoppingCart::getId,id);
        wrapper.eq(shoppingCart::getDishId,cart.getDishId());

        shoppingCart existCart = cartService.getOne(wrapper);

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
    public Result<shoppingCart> sub(@RequestBody shoppingCart cart , HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<shoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(shoppingCart::getUserId,id);

        shoppingCart existCart = cartService.getOne(wrapper);
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
    public Result<List<shoppingCart>> list(HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<shoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(shoppingCart::getUserId,id);
        wrapper.orderByDesc(shoppingCart::getCreateTime);

        List<shoppingCart> list = cartService.list(wrapper);

        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result<String> clean(HttpServletRequest request){
        Long id = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<shoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(shoppingCart::getUserId,id);

        cartService.remove(wrapper);

        return Result.success("清空成功");

    }
}
