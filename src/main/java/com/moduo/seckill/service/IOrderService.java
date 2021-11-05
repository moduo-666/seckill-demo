package com.moduo.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moduo.seckill.pojo.Order;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.vo.GoodsVo;
import com.moduo.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Wuzicong
 * @since 2021-10-18
 */
public interface IOrderService extends IService<Order> {
    /**
     * 秒杀创建订单
     * @param user
     * @param good
     * @return
     */
    Order seckill(User user, GoodsVo good);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Long orderId);

    /**
     * 生成秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(User user, Long goodsId);

    /**
     * 验证秒杀地址
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkPath(User user,Long goodsId,String path);
}
