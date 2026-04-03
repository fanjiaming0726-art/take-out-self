package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.fjm0313_takeout_self.entity.Category;
import com.example.fjm0313_takeout_self.mapper.CateGoryMapper;
import com.example.fjm0313_takeout_self.service.CateGoryService;
import org.springframework.stereotype.Service;

@Service
public class CateGoryServiceImpl extends ServiceImpl<CateGoryMapper, Category> implements CateGoryService {
}
