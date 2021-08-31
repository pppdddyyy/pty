package com.usian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.pojo.OrderItem;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 韩丛
 * @since 2021-08-06
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    // 查询超时订单，对应的所有的订单项集合
    public List<OrderItem> queryOverTimeOrderItems();
}
