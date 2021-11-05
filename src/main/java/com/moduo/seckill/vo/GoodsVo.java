package com.moduo.seckill.vo;

import com.moduo.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品返回对象
 * @author Wu Zicong
 * @create 2021-10-18 9:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods{
    /**
     * 秒杀价
     */
    private BigDecimal seckillPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private Date startDate;

    /**
     * 秒杀结束时间
     */
    private Date endDate;

}
