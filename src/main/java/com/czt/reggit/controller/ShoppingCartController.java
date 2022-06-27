package com.czt.reggit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czt.reggit.common.BaseContext;
import com.czt.reggit.common.R;
import com.czt.reggit.pojo.ShoppingCart;
import com.czt.reggit.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据：{}",shoppingCart);

        //设置用户id，指定当前是哪个用户添加数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询当前菜品或套餐，是否在购物车中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,currentId);

        if (dishId != null){
            //添加到购物车是菜品
            lqw.eq(ShoppingCart::getDishId,dishId);

        } else{
            //添加到购物车是套餐
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        }

        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(lqw);


        if (shoppingCartServiceOne != null){
            //如果已存在，数量加一
            Integer number = shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber( number + 1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }else {
            //如果不存在，则添加进购物车，数量默认为一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne = shoppingCart;

        }

        return R.success(shoppingCartServiceOne);

    }


    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        lqw.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lqw);

        return R.success(shoppingCartList);
    }


    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartService.remove(lqw);

        return R.success("清空购物车");
    }


    /**
     * 减少商品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("购物数量减一：{}",shoppingCart);


        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        //前端传来的数据只有菜品id或者套餐id，所以数量需要自己查询
        if (shoppingCart.getDishId() != null){
            //购物车减一的是菜品
            lqw.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            //购物车减一的是套餐
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //查询减一的商品的数量
        ShoppingCart one = shoppingCartService.getOne(lqw);

        Integer number = one.getNumber();

        if (number == 1){
            //数量为1，减1数量时，需要顺便删除商品
            shoppingCartService.removeById(one);
        } else{
            //数量不为1，可以只减一数量
            one.setNumber(number - 1);
            shoppingCartService.updateById(one);
        }

        return R.success(one);


    }
}
