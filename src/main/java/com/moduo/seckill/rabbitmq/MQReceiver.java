package com.moduo.seckill.rabbitmq;

import com.alibaba.druid.util.StringUtils;
import com.moduo.seckill.pojo.SeckillMessage;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.service.IGoodsService;
import com.moduo.seckill.service.IOrderService;
import com.moduo.seckill.util.JsonUtil;
import com.moduo.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Wu Zicong
 * @create 2021-11-02 9:35
 */
@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg){
        log.info("【秒杀】接受消息："+ msg);
        SeckillMessage message = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);
        Long goodsId = message.getGoodsId();
        User user = message.getUser();
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount()<1){
            log.info("【数据库】库存不足");
            return;
        }
        //判断是否重复抢购
        String seckillOrderJson = (String)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(!StringUtils.isEmpty(seckillOrderJson)){
            log.info("一人限购一件");
            return;
        }
        orderService.seckill(user,goods);
    }
}
