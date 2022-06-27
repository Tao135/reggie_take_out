package com.czt.reggit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czt.reggit.dao.ShoppingCartMapper;
import com.czt.reggit.pojo.ShoppingCart;
import com.czt.reggit.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
