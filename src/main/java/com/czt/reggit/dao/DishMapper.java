package com.czt.reggit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czt.reggit.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
