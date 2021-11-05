package com.moduo.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moduo.seckill.pojo.Goods;
import com.moduo.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Wuzicong
 * @since 2021-10-18
 */
public interface IGoodsService extends IService<Goods> {
    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
