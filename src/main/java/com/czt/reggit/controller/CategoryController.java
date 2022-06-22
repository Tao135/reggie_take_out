package com.czt.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czt.reggit.common.R;
import com.czt.reggit.pojo.Category;
import com.czt.reggit.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Category::getSort);

        //进行分页查询
        categoryService.page(pageInfo,lqw);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类，id为：{}",ids);


        categoryService.remove(ids);
        return R.success("删除分类成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改的数据是：{}",category);
        categoryService.updateById(category);

        return R.success("修改分类成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> catelqw = new LambdaQueryWrapper<>();
        //添加条件
        catelqw.eq(category.getType() != null,Category::getType,category.getType());    //当传过来的category的type值不为空时，进行实体类的type与传来的type进行比对条件判定。
        //添加排序条件
        catelqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);             //首先以sort为第一条件进行排序，然后再以updateTime为第二条件进行排序

        List<Category> list = categoryService.list(catelqw);
        return R.success(list);

    }
}
