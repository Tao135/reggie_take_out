package com.czt.reggit.dto;

import com.czt.reggit.pojo.OrderDetail;
import com.czt.reggit.pojo.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;
}
