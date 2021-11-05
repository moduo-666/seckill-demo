package com.moduo.seckill.rabbitmq;

import com.moduo.seckill.common.rabbitMQEnum.RabbitMQEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * mq生产者
 * @author Wu Zicong
 * @create 2021-11-02 9:32
 */
@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private final String MSG_SECKILL_ROUTINGKEY = "seckill.msg";
    /**
     * rabbitMq Demo
     * @param msg
     */
    public void send(Object msg){
        log.info("【DEMO】发送消息：" + msg );
        rabbitTemplate.convertAndSend("queue",msg);
    }

    /**
     * 秒杀消息生产者
     * @param message
     */
    public void sendSeckillMessage(String message){
        rabbitTemplate.convertAndSend(RabbitMQEnum.SECKILL_EXCHANGE.getValue(),
                                      MSG_SECKILL_ROUTINGKEY,
                                      message);
        log.info("【秒杀】发送消息：{}" , message);
    }
}
