package com.moduo.seckill.vo;

import com.moduo.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wu Zicong
 * @create 2021-10-26 11:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {

    private User user;

    private GoodsVo goodsVo;

    private int secKillStatus;

    private int remainSeconds;
}
