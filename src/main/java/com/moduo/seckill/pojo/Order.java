package com.moduo.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Wuzicong
 * @since 2021-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Integer goodsId;

    /**
     * 收获地址ID
     */
    private Long deliverAddrId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 设备信息
     */
    private Integer orderChannel;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单创建时间
     */
    private Date createDate;

    /**
     * 支付时间
     */
    private Date payDate;


}
