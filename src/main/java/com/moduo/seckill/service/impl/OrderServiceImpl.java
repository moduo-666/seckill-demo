package com.moduo.seckill.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.moduo.seckill.exception.GlobalException;
import com.moduo.seckill.mapper.GoodsMapper;
import com.moduo.seckill.mapper.OrderMapper;
import com.moduo.seckill.pojo.Order;
import com.moduo.seckill.pojo.SeckillGoods;
import com.moduo.seckill.pojo.SeckillOrder;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.service.IGoodsService;
import com.moduo.seckill.service.IOrderService;
import com.moduo.seckill.service.ISeckillGoodsService;
import com.moduo.seckill.service.ISeckillOrderService;
import com.moduo.seckill.util.JsonUtil;
import com.moduo.seckill.util.MD5Util;
import com.moduo.seckill.util.UUIDUtil;
import com.moduo.seckill.vo.GoodsVo;
import com.moduo.seckill.vo.OrderDetailVo;
import com.moduo.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wuzicong
 * @since 2021-10-18
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Order seckill(User user, GoodsVo good) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //秒杀商品表预减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>()
                                .eq("goods_id", good.getId()));
        //gt 大于：大于0时减库存
        boolean seckillGoodsResult = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count=stock_count-1")
                .eq("id",seckillGoods.getId())
                .gt("stock_count",0));
        if(seckillGoods.getStockCount() < 1){
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + good.getId(),"0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(good.getId());
        order.setDeliverAddrId(0L);
        order.setGoodsName(good.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(good.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(good.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrderService.save(seckillOrder);
        //存入商品编号与用户编号绑定存入redis，防止重复抢购
        valueOperations.set("order:"+ user.getId() + ":" +  good.getId(), JsonUtil.object2JsonStr(seckillOrder));
        return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if(order == null)
            return null;
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(Long.valueOf(order.getGoodsId()));
        OrderDetailVo detail = new OrderDetailVo();
        detail.setGoodsVo(goodsVo);
        detail.setOrder(order);
        return detail;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        //秒杀地址有效时间60s
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId,str,60, TimeUnit.SECONDS);
        return str;
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if(user == null || StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath = (String)redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }
}
