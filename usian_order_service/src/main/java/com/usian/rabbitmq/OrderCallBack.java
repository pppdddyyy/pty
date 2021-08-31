package com.usian.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: OrderCallBack
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/27 8:57
 */
@Component
public class OrderCallBack implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate amqpTemplate;

    // 所有订单，库存修改的值
    /**
     *   001          map
     *   002          map
     *   3          map
     */
    private Map<String, Map<String, Integer>> orderItemNums = new HashMap<>();
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        if(!ack){//发送失败
            String orderId = correlationData.getId();
            //重新发送消息
            amqpTemplate.convertAndSend("item_kucun_exchang","update.num",orderItemNums.get(orderId));
        }

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

    }

    public Map<String, Map<String, Integer>> getOrderItemNums() {
        return orderItemNums;
    }

    public void setOrderItemNums(Map<String, Map<String, Integer>> orderItemNums) {
        this.orderItemNums = orderItemNums;
    }
}
