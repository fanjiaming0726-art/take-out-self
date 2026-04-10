package com.example.fjm0313_takeout_self.controller.seller;

import com.example.fjm0313_takeout_self.common.LoginRequired;
import com.example.fjm0313_takeout_self.common.Result;
import com.example.fjm0313_takeout_self.entity.SeckillActivity;
import com.example.fjm0313_takeout_self.service.SeckillService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seller/seckill")
public class SellerSeckillController {

    @Autowired
    private SeckillService seckillService;


    @LoginRequired("EMPLOYEE")
    @PostMapping("/list")
    public Result<List<SeckillActivity>> list(){
        return Result.success(seckillService.listActivities());
    }

    @LoginRequired("EMPLOYEE")
    @PostMapping("/rush/{activityId}")
    public Result<String> load(@PathVariable Long activityId){
        seckillService.loadActivityToRedis(activityId);
        return Result.success("该菜品已加载到秒杀活动");
    }




}
