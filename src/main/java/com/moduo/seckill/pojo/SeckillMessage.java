package com.moduo.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wu Zicong
 * @create 2021-11-02 10:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMessage {
    private User user;
    private Long goodsId;

}
