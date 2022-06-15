package com.czt.reggit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czt.reggit.dto.DishDto;
import com.czt.reggit.pojo.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据,需要操作两张表，dish.dish_flavor
    public void saveWithFlavor(DishDto dishDto);
}
