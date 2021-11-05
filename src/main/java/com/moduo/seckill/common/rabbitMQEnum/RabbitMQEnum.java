package com.moduo.seckill.common.rabbitMQEnum;

/**
 * @author Wu Zicong
 * @create 2021-11-02 11:20
 */

public enum RabbitMQEnum {
    /*秒杀交换机*/
    SECKILL_EXCHANGE("SECKILL_EXCHANGE","seckillExchange"),
    /*秒杀队列*/
    SECKILL_QUEUE("SECKILL_QUEUE","seckillQueue"),
    /*秒杀路由key*/
    SECKILL_ROUTINGKEY("SECKILL_ROUTINGKEY","seckill.#");
    private String name;
    private String value;

    RabbitMQEnum(String name,String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
