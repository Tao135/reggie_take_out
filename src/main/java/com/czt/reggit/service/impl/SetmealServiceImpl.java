package com.czt.reggit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czt.reggit.common.CustomException;
import com.czt.reggit.dao.SetmealMapper;
import com.czt.reggit.dto.SetmealDto;
import com.czt.reggit.pojo.Setmeal;
import com.czt.reggit.pojo.SetmealDish;
import com.czt.reggit.service.SetmealDishService;
import com.czt.reggit.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时保留套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId,ids);
        lqw.eq(Setmeal::getStatus,1);

        int count = (int) this.count(lqw);
        if (count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }


        //如果可以删除，先删除套餐表中的数据--Setmeal
        this.removeByIds(ids);


        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        //删除关系表中的数据--setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);
    }

    /**
     * 修改套装状态,1起售，0禁止
     * @param ids
     * @param state
     */
    @Override
    public void updateByStatus(Long ids, int state) {
        //添加条件构造器
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getId,ids);

        Setmeal setmeal = getOne(lqw);

        setmeal.setStatus(state);

        this.update(setmeal,lqw);

    }
}
