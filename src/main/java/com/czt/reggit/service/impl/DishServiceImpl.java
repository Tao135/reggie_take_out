package com.czt.reggit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czt.reggit.dao.DishMapper;
import com.czt.reggit.dto.DishDto;
import com.czt.reggit.pojo.Dish;
import com.czt.reggit.pojo.DishFlavor;
import com.czt.reggit.service.DishFlavorService;
import com.czt.reggit.service.DishService;
import org.springframework.beans.BeanUtils;
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

    /**
     * 根据id，查询菜品和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);     //将dish的信息，copy给dishDto

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavorList = dishFlavorService.list(lqw);
        dishDto.setFlavors(flavorList);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //更新dish_flavor信息
        //清理当前菜对应口味信息
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(lqw);

        //添加当前提交过来的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();

        //Stream流效率高、这样是动态的为每一个菜品口味，添加id。
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
