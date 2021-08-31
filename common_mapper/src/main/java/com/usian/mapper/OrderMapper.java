package com.usian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.pojo.Order;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 韩丛
 * @since 2021-08-06
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 查询下单时间超过30分钟的 订单的订单号信息
     * @return
     */
//    public List<String> queryOverTimeOrderIds();

    /**
     * 关闭超时订单
     * @return
     */
    public int closeOverTimeOrders();




}
