package com.moduo.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moduo.seckill.pojo.Goods;
import com.moduo.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Wuzicong
 * @since 2021-10-18
 */
public interface GoodsMapper extends BaseMapper<Goods> {
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
    GoodsVo findGoodsVoByGoodsId(@Param("goodsId") Long goodsId);
}
