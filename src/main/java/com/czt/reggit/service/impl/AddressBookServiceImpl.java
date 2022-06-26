package com.czt.reggit.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czt.reggit.dao.AddressBookMapper;
import com.czt.reggit.pojo.AddressBook;
import com.czt.reggit.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
