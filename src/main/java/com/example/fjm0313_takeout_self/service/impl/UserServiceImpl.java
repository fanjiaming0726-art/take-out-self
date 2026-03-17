package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fjm0313_takeout_self.entity.User;
import com.example.fjm0313_takeout_self.mapper.UserMapper;
import com.example.fjm0313_takeout_self.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
