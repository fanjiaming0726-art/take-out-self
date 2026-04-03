package com.example.fjm0313_takeout_self.service.impl;

import com.example.fjm0313_takeout_self.entity.Category;
import com.example.fjm0313_takeout_self.mapper.CateGoryMapper;
import com.example.fjm0313_takeout_self.service.CateGoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CateGoryServiceImpl implements CateGoryService {

    @Autowired
    private CateGoryMapper cateGoryMapper;

    @Override
    public void addCategory(Category category) {
        cateGoryMapper.insert(category);
    }

    @Override
    public void updateCategory(Category category) {
        cateGoryMapper.updateById(category);
    }

    @Override
    public Category findById(Long id) {
        return cateGoryMapper.selectById(id);
    }

    @Override
    public List<Category> findAll() {
        return cateGoryMapper.selectList(null);
    }
}