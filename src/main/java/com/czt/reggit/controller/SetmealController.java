package com.czt.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czt.reggit.common.R;
import com.czt.reggit.dto.SetmealDto;
import com.czt.reggit.pojo.Category;
import com.czt.reggit.pojo.Setmeal;
import com.czt.reggit.service.CategoryService;
import com.czt.reggit.service.SetmealDishService;
import com.czt.reggit.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);

        setmealService.saveWithDish(setmealDto);
        return R.success("|");
    }


    /**
     * 套餐分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        lqw.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,lqw);

        //进行对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");       //将pageInfo的信息拷贝到dtoPage，但records的属性不用拷贝。
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }


    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);

        setmealService.removeWithDish(ids);
        return R.success("删除套餐数据成功");
    }


    /**
     * 修改销售状态
     * @param state
     * @param ids
     * @return
     */
    @PostMapping("/status/{state}")
    public R<String> update(@PathVariable("state") int state,@RequestParam Long ids){
        log.info("state:{}",state + "-----id:{}",ids);

        //准备将套餐状态修改，1
        setmealService.updateByStatus(ids,state);

        return R.success("修改销售状态");
    }
}
