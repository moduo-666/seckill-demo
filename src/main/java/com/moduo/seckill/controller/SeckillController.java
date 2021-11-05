package com.moduo.seckill.controller;

import com.alibaba.druid.util.StringUtils;
import com.moduo.seckill.common.annotation.AccessLimit;
import com.moduo.seckill.pojo.SeckillMessage;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.rabbitmq.MQSender;
import com.moduo.seckill.service.IGoodsService;
import com.moduo.seckill.service.IOrderService;
import com.moduo.seckill.service.ISeckillOrderService;
import com.moduo.seckill.util.JsonUtil;
import com.moduo.seckill.util.ThreadLocalUtil;
import com.moduo.seckill.vo.GoodsVo;
import com.moduo.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wu Zicong
 * @create 2021-10-19 11:00
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private MQSender mqSender;
    @Autowired
    @Qualifier("seckillScript")
    private DefaultRedisScript script;
    //库存为空标识（减少访问redis）
    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();
    /**
     * 秒杀
     * @param path 秒杀路径
     * @param goodsId 商品id
     * @return
     */

    @AccessLimit(second = 5,maxCount = 5)
    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, Long goodsId){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        User user = ThreadLocalUtil.getUser();
//        检查秒杀地址与是否登录
        if(!orderService.checkPath(user,goodsId,path)){
            return RespBean.info(400,"秒杀地址过期，请重新登录");
        }
        //内存标记，减少Redis访问
        if(EmptyStockMap.get(goodsId)!=null && EmptyStockMap.get(goodsId)){
            return RespBean.info(400,"【标记】库存不足");
        }
        //判断是否重复抢购
        String seckillOrderJson = (String)valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if(!StringUtils.isEmpty(seckillOrderJson)){
            return RespBean.info(400,"一人限购一件");
        }

        GoodsVo good = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断时间
        if(good.getEndDate().before(new Date())){
            return RespBean.info(400,"秒杀时间已过");
        }
//        //判断库存
//        if(good.getStockCount()<1){
//            return RespBean.info(400,"库存不足");
//        }
        //预减库存
        Long stock = (Long)redisTemplate.execute(script,
                                               Collections.singletonList("seckillGoods:" + goodsId),
                                               Collections.EMPTY_LIST);
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        if(stock < 1){
            EmptyStockMap.put(goodsId,true);
//            valueOperations.decrement("seckillGoods:" + goodsId);
            return RespBean.info(400,"【预减】库存不足");
        }
        //请求入队，立即返回排队中
        SeckillMessage message = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(message));
        return RespBean.success(0);
    }

    /**
     * 查看秒杀结果
     * @param goodsId
     * @return
     */
    @AccessLimit(second = 5,maxCount = 5)
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(Long goodsId){
        User user = ThreadLocalUtil.getUser();
        if(user == null){
            return RespBean.info(400,"请先登录");
        }
        Long orderId = seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }

    /**
     * 获取秒杀地址
     * @param goodsId
     * @return
     */
    @AccessLimit(second = 5,maxCount = 5)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(Long goodsId){
        User user = ThreadLocalUtil.getUser();
        String str = orderService.createPath(user,goodsId);
        return RespBean.success(str);
    }
}
