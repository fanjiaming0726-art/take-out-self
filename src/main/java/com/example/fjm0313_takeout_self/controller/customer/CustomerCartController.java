package com.example.fjm0313_takeout_self.controller.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.entity.ShoppingCart;
import com.example.fjm0313_takeout_self.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customer/shoppingCart")
public class CustomerCartController {

    @Autowired
    private ShoppingCartService cartService;

    // 添加
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart cart, HttpServletRequest request){
        // 找出用户的订单，找有没有这道菜，有就把数量＋1，没有就新加
        Long userId = getUserId(request);
        cart.setUserId(userId);

        ShoppingCart existCart = cartService.findExistItem(userId,cart.getDishId(),cart.getFlavor(),cart.getPortion());

        if(existCart != null){
            existCart.setNumber(existCart.getNumber() + 1);
            cartService.updateCartItem(existCart);
            return Result.success(existCart);
        }else{
            cart.setNumber(1);
            cartService.addCartItem(cart);
            return Result.success(cart);
        }
    }

    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart cart , HttpServletRequest request){
        Long userId = getUserId(request);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();

        ShoppingCart existCart = cartService.findExistItem(userId,cart.getDishId(),cart.getFlavor(),cart.getPortion());
        if(existCart == null){
            return Result.fail("购物单不存在");

        }
        int number = existCart.getNumber() - 1;

        if(number > 0){
            existCart.setNumber(number);
            cartService.updateCartItem(existCart);
            return Result.success(existCart);
        }else{
            cartService.removeCartItem(existCart.getId());
            return Result.success(null);
        }


    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(HttpServletRequest request){
        Long userId = getUserId(request);
        List<ShoppingCart> list = cartService.findByUserId(userId);

        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result<String> clean(HttpServletRequest request){
        Long userId = getUserId(request);

        cartService.clearByUserId(userId);

        return Result.success("清空成功");

    }

    private Long getUserId(HttpServletRequest request){
        return (Long) request.getSession().getAttribute("userId");
    }
}
