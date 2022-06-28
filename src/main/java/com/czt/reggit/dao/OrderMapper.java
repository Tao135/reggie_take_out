package com.czt.reggit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czt.reggit.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}