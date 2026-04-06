package com.example.fjm0313_takeout_self.service.impl;

import com.example.fjm0313_takeout_self.service.RedisLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisLimitServiceImpl implements RedisLimitService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public boolean isAllowed(Long userId, int maxRequest, int windowSeconds) {
        String key = "rate:limit" +userId;

        Long count = redisTemplate.opsForValue().increment(key);
        if(count == 1){
            redisTemplate.expire(key,windowSeconds, TimeUnit.SECONDS);
        }

        return count <= maxRequest;
    }
}
