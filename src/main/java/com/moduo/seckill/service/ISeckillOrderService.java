package com.moduo.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moduo.seckill.pojo.SeckillOrder;
import com.moduo.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Wuzicong
 * @since 2021-10-18
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {
    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return
     */
    Long getResult(User user, Long goodsId);
}
