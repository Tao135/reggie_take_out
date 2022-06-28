package com.czt.reggit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czt.reggit.dao.OrderDetailMapper;
import com.czt.reggit.pojo.OrderDetail;
import com.czt.reggit.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}