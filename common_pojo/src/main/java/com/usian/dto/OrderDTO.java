package com.usian.dto;

import com.usian.pojo.Order;
import com.usian.pojo.OrderItem;
import com.usian.pojo.OrderShipping;

import java.util.List;

/**
 * @Title: OrderDTO
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/25 9:58
 */
public class OrderDTO {

    OrderShipping orderShipping;
    Order order;
    List<OrderItem> orderItems;

    public OrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(OrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
