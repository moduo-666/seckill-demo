package com.moduo.seckill.config;

import com.moduo.seckill.common.rabbitMQEnum.RabbitMQEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wu Zicong
 * @create 2021-11-02 9:25
 */
@Configuration
public class RabbitMQConfig {
    /**
     * seckill队列注入
     * @return
     */
    @Bean
    public Queue seckillQueue(){
        return new Queue(RabbitMQEnum.SECKILL_QUEUE.getValue());
    }

    /**
     * seckill主题模式交换机
     * @return
     */
    @Bean
    public TopicExchange seckillTopicExchange(){
        return new TopicExchange(RabbitMQEnum.SECKILL_EXCHANGE.getValue());
    }

    /**
     * 绑定Bean，绑定秒杀交换机、队列、路由key
     * @return
     */
    @Bean
    public Binding binding01(){
        return BindingBuilder.bind(seckillQueue())
                             .to(seckillTopicExchange())
                             .with(RabbitMQEnum.SECKILL_ROUTINGKEY.getValue());
    }
}
