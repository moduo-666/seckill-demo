package com.moduo.seckill.controller;


import com.moduo.seckill.pojo.User;
import com.moduo.seckill.service.IOrderService;
import com.moduo.seckill.util.ThreadLocalUtil;
import com.moduo.seckill.vo.OrderDetailVo;
import com.moduo.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Wuzicong
 * @since 2021-10-18
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(Long orderId){
        User user = ThreadLocalUtil.getUser();
        if(user==null){
            return RespBean.info(400,"请先登录");
        }
        OrderDetailVo detailVo = orderService.detail(orderId);
        if(detailVo == null){
            return RespBean.info(400,"查询失败");
        }
        return RespBean.success(detailVo);
    }
}
