package com.czt.reggit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czt.reggit.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
