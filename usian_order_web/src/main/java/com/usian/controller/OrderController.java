package com.usian.controller;

import com.usian.api.OrderFeign;
import com.usian.dto.OrderDTO;
import com.usian.pojo.Order;
import com.usian.pojo.OrderItem;
import com.usian.pojo.OrderShipping;
import com.usian.utils.JsonUtils;
import com.usian.utils.Result;
import com.usian.vo.CartOrOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title: OrderController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/24 15:04
 */
@RequestMapping("/frontend/order")  //frontend/order/goSettlement
@RestController
public class OrderController {

    @Autowired
    private OrderFeign orderFeign;

    @RequestMapping("/insertOrder")
    public Result insertOrder(OrderShipping orderShipping, Order order, String orderItem){ // orderItem json数组----》 java类型

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrder(order);
        orderDTO.setOrderShipping(orderShipping);
        orderDTO.setOrderItems(JsonUtils.jsonToList(orderItem,OrderItem.class));

       String orderId =  orderFeign.insertOrder(orderDTO);
        return Result.ok(orderId);
    }


    /**
     * 购物车
     *    A      2
     *  **  B      3
     *  **  C      1
     *          去结算
     *
     *  去结算页面/ 去订单的确认页面
     *   查询订单确认页面中需要的 订单项信息  List<CartOrOrderItem>
     * @param ids
     * @param userId
     * @param token
     * @return
     */
    @RequestMapping("goSettlement")//frontend/order/goSettlement
    public Result goSettlement(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId, @RequestParam("token") String token){
        List<CartOrOrderItem> data = orderFeign.goSettlement(ids,userId);
        return Result.ok(data);
    }


}
