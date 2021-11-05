package com.moduo.seckill.vo;

import com.moduo.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wu Zicong
 * @create 2021-11-01 11:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {
    private Order order;
    private GoodsVo goodsVo;
}
