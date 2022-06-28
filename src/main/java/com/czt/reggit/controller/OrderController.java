package com.czt.reggit.controller;

import com.czt.reggit.common.R;
import com.czt.reggit.pojo.Orders;
import com.czt.reggit.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(Orders orders){
        log.info("订单数据");
        orderService.submit(orders);

        return R.success("下单成功");
    }
}
