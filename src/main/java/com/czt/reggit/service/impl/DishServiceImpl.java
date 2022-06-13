package com.czt.reggit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czt.reggit.dao.DishMapper;
import com.czt.reggit.pojo.Dish;
import com.czt.reggit.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{
}
