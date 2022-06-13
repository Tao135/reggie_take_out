package com.czt.reggit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czt.reggit.common.CustomException;
import com.czt.reggit.dao.CategoryMapper;
import com.czt.reggit.pojo.Category;
import com.czt.reggit.pojo.Dish;
import com.czt.reggit.pojo.Setmeal;
import com.czt.reggit.service.CategoryService;
import com.czt.reggit.service.DishService;
import com.czt.reggit.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishlqw = new LambdaQueryWrapper<>();
        //添加查询条件
        dishlqw.eq(Dish::getCategoryId,id);
        Long count1 = dishService.count(dishlqw);

        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if (count1 > 0){
            //已经关联了菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmeallqw = new LambdaQueryWrapper<>();
        //添加查询条件
        setmeallqw.eq(Setmeal::getCategoryId,id);
        Long count2 = setmealService.count(setmeallqw);
        if (count2 > 0){
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(id);
    }
}
