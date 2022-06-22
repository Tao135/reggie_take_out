package com.czt.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czt.reggit.common.R;
import com.czt.reggit.dto.DishDto;
import com.czt.reggit.pojo.Category;
import com.czt.reggit.pojo.Dish;
import com.czt.reggit.service.CategoryService;
import com.czt.reggit.service.DishFlavorService;
import com.czt.reggit.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("新增的菜品为：{}",dishDto);

        dishService.saveWithFlavor(dishDto);
        return R.success("新增成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> pageDto = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null,Dish::getName,name);
        //添加排序条件
        lqw.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,lqw);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,pageDto,"records");     //将pageInfo中除了records的属性拷贝到pageDto

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);     //将item的值拷贝到dishDto

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        pageDto.setRecords(list);

        return R.success(pageDto);
    }


    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    /**
     * 修改菜品
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);
        return R.success("新增成功");
    }


    /**
     * 根据条件查询对应的菜品信息
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){

        //构造查询条件
        LambdaQueryWrapper<Dish> dishlqw = new LambdaQueryWrapper<>();
        dishlqw.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（可以出售）的菜品
        dishlqw.eq(Dish::getStatus,1);

        //排序条件
        dishlqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(dishlqw);

        return R.success(list);
    }
}
