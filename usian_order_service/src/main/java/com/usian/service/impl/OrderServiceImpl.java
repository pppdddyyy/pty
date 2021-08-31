package com.usian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.api.CartFeign;
import com.usian.dto.OrderDTO;
import com.usian.mapper.OrderItemMapper;
import com.usian.mapper.OrderMapper;
import com.usian.mapper.OrderShippingMapper;
import com.usian.pojo.Order;
import com.usian.pojo.OrderItem;
import com.usian.pojo.OrderShipping;
import com.usian.rabbitmq.OrderCallBack;
import com.usian.service.OrderService;
import com.usian.vo.CartOrOrderItem;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.sql.SQLOutput;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 韩丛
 * @since 2021-08-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderShippingMapper orderShippingMapper;


    @Autowired
    private RabbitTemplate amqpTemplate;

    @Autowired
    private OrderCallBack orderCallBack;
    @Override
    public List<CartOrOrderItem> queryOrderItems(Long userId, Long[] ids) {// 2,3
        //查询 订单项的集合
        //1. 获取当前登录人 购物车中所有的数据
        Collection<CartOrOrderItem> cartItems = cartFeign.showCart(userId);// A B C
        //2. 筛选出，购买的订单项的数据
        List<CartOrOrderItem> orderItems = new ArrayList<>();//订单项集合
        for (Long id: ids){// 2
            for (CartOrOrderItem item : cartItems){
                if(item.getId() .equals( id)){
                    orderItems.add(item);
                }
            }
        }

        return orderItems;
    }

    /**
     *
     * 下订单：
     *  上游： 订单端   修改库存的消息   上游服务把消息成功  发送
     *  下游：  商品端     下游服务成功把消息成功消费  对消息做幂等
     * 新增订单业务层接口
     * @param orderDTO
     * @return
     */

    /**
     *   张三  ：0001
     *       小米手环            2个
     *       苹果电脑            1个
     *     item-service
     *                 小米手环            -2个          0001
     *                 苹果电脑            -1个
     *  王五 ：0002
     *         小米手环      1个
     *         华为手机      1个
     *      item-service
     *                 小米手环      -1个               0002
     *                 华为手机      -1个
     *
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public String insertOrder(OrderDTO orderDTO) {
        // 生成订单相关的记录
        // part1：  新增到order
        // 生成一个唯一的订单号
        String orderId = UUID.randomUUID().toString()+new Random().nextInt(100);
        Date now = new Date();
        Order order = orderDTO.getOrder();
        order.setOrderId(orderId);
        order.setStatus(1);
        order.setCreateTime(now);
        this.save(order);


        HashMap<String, Integer> itemNums = new HashMap<>();
        //part2: 新增到 order_item
        List<OrderItem> orderItems = orderDTO.getOrderItems();
        for (OrderItem orderItem : orderItems){// A 1  B 2
            String orderItemID = new Date().getTime() + new Random().nextInt(100)+"";
            orderItem.setId(orderItemID);
            orderItem.setOrderId(orderId);
            orderItemMapper.insert(orderItem);
            itemNums.put(orderItem.getItemId(),orderItem.getNum());
        }
        //part3: 新增到 order_shipping
        OrderShipping orderShipping = orderDTO.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(now);
        orderShippingMapper.insert(orderShipping);

             /*
               最终消息一致型解决方案：
                  1.  消息一定要发送出去  *
                    mq发送消息的应答机制+重试机制
                  2. 消费者一定能够消费到消息   *
                    消息持久化+手动ACK
                  3. 消费者不能够重复的消费
            *
            * */
        // 订单触发其他的关联操作
        // 修改库存    update tb_item  set  num = num - ??? where item_id = ???   item_service  Map 集合
            // order service 调用你的 item service   openfeign  http原生客户端也可以    mq---RabbitMQ
        // mq应答机制+ 定时器
//        amqpTemplate.setxxx();  1订单   --- itemNums1            2订单-----itemNum2

        orderCallBack.getOrderItemNums().put(orderId,itemNums);
        amqpTemplate.setReturnCallback(orderCallBack);
        amqpTemplate.setConfirmCallback(orderCallBack);//监听回调对象
        CorrelationData correlationData = new CorrelationData(orderId);
        amqpTemplate.convertAndSend("item_kucun_exchang","update.num",itemNums,correlationData);


        // 修改购物车的数据  扩展
        // xxx


        return orderId;
    }


}
