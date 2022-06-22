package com.czt.reggit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czt.reggit.dto.SetmealDto;
import com.czt.reggit.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //新增套餐，同时保留套餐和菜品的关联关系
    public void saveWithDish(SetmealDto setmealDto);

    //删除套餐，同时删除套餐和菜品的关联数据
    public void removeWithDish(List<Long> ids);

    //更改销售状态
    public void updateByStatus(Long ids,int state);
}
