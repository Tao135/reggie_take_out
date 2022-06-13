package com.czt.reggit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czt.reggit.pojo.Category;


public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
