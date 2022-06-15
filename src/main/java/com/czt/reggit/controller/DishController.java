package com.czt.reggit.controller;

import com.czt.reggit.common.R;
import com.czt.reggit.dto.DishDto;
import com.czt.reggit.pojo.Dish;
import com.czt.reggit.service.DishFlavorService;
import com.czt.reggit.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;

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
}
