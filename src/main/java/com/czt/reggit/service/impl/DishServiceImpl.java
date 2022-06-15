package com.czt.reggit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czt.reggit.dao.DishMapper;
import com.czt.reggit.dto.DishDto;
import com.czt.reggit.pojo.Dish;
import com.czt.reggit.pojo.DishFlavor;
import com.czt.reggit.service.DishFlavorService;
import com.czt.reggit.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时保存口味数据
     * @param dishDto
     */
    @Transactional          //添加事务
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();  //菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        //Stream流效率高、这样是动态的为每一个菜品口味，添加id。
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());


        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);      //批量保存
    }
}
