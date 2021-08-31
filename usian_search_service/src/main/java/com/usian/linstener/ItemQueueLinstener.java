package com.usian.linstener;

import com.rabbitmq.client.Channel;
import com.usian.service.ESService;
import com.usian.vo.ItemVO;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Title: ItemQueueLinstener
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/18 16:08
 */
@Component
public class ItemQueueLinstener {

    @Autowired
    private ESService esService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="item_queue",durable = "true"),
            exchange = @Exchange(value="usian_item",type= ExchangeTypes.TOPIC),
            key= {"insert.item"}
    ))
    public void listen(ItemVO itemVO, Channel channel, Message message){
        // 将数据保存到es
        esService.insertItem(itemVO);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
