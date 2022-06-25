package com.czt.reggit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.czt.reggit.dao.UserMapper;
import com.czt.reggit.pojo.User;
import com.czt.reggit.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
