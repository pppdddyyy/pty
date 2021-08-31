package com.usian.task;

import com.usian.mapper.OrderItemMapper;
import com.usian.mapper.OrderMapper;
import com.usian.pojo.Item;
import com.usian.pojo.OrderItem;
import com.usian.util.RedisClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Title: OrderOverTimeTask
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/25 14:33
 */
@Component
public class OrderOverTimeTask {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private RedisClient redisClient;
    /**
     * 干啥？ 关闭超时的订单
     * 啥时启动？ 项目启动时我就启动
     *  周期？1分钟     30分
     *  @Transactional
     *   A(){
     *       B();
     *   }
     *    @Transactional(propagation = Propagation.MANDATORY)
     *   B(){
     *
     *   }
     *
     */
    @Scheduled(cron="0/1 * * * * ?")
    @Transactional(propagation = Propagation.NEVER,isolation = Isolation.READ_COMMITTED )
    public void closeOverTimeOrder(){
        if(redisClient.setNx("ITEM_OVER_TIME_ORDER_LOCK","",5, TimeUnit.SECONDS)){// 解决缓存的击穿
            try {
                //  3.  将库存在回退到商品里面  update tb_item set num = num - 退回的 where id = 超时订单管理的订单项中的商品的ID
                //根据关闭的订单，获取对应的订单项集合
                // SELECT * FROM tb_order_item WHERE order_id IN (SELECT order_id FROM `tb_order` WHERE TIMESTAMPDIFF(MINUTE,create_time,NOW())>=30)

                List<OrderItem> orderItems = orderItemMapper.queryOverTimeOrderItems();
                HashMap<String, Integer> itemNums = new HashMap<>();
                //part2: 新增到 order_item
                for (OrderItem orderItem : orderItems){// A 1  B 2
                    itemNums.put(orderItem.getItemId(),-orderItem.getNum());
                }
                amqpTemplate.convertAndSend("item_kucun_exchang","update.num",itemNums);//

        /*
           1. 获取哪些是超时的订单
           2. 将超时的订单关闭  update tb_order set status = 6 where order_id in(SELECT order_id FROM `tb_order` WHERE TIMESTAMPDIFF(MINUTE,create_time,NOW())>=30)
         */
                orderMapper.closeOverTimeOrders();
            } catch (Exception e) {
                // 释放锁
            }finally {
                redisClient.del("ITEM_OVER_TIME_ORDER_LOCK");
            }

        }







    }
}
