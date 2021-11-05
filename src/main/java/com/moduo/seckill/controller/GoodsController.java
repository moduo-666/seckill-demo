package com.moduo.seckill.controller;

import com.moduo.seckill.common.annotation.AccessLimit;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.service.IGoodsService;
import com.moduo.seckill.service.IUserService;
import com.moduo.seckill.util.ThreadLocalUtil;
import com.moduo.seckill.vo.DetailVo;
import com.moduo.seckill.vo.GoodsVo;
import com.moduo.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 商品
 * @author Wu Zicong
 * @create 2021-10-14 10:55
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    /**
     * 跳转商品列表页
     * @return
     */
    @AccessLimit(second = 5,maxCount = 5,needLogin = false)
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, HttpServletRequest request, HttpServletResponse response){
        //Redis中获取页面
        ValueOperations valueOperations = this.redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
//        if(!StringUtils.isEmpty(html))
//            return html;
        //如果为空，手动渲染，并存入redis
//        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        WebContext webContext = new WebContext(request,
                                               response,
                                               request.getServletContext(),
                                               request.getLocale(),
                                               model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",webContext);
//        if(!StringUtils.isEmpty(html)){
//            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
//        }
        return html;
    }
    @AccessLimit(second = 5,maxCount = 5,needLogin = true)
    @RequestMapping(value = "/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(@PathVariable String goodsId){
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(Long.valueOf(goodsId));
        User user = ThreadLocalUtil.getUser();
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date now = new Date();
        int seckillStatus = 0;
        int remainSeconds = 0;
        if(now.before(startDate)){
            //秒杀倒计时
            seckillStatus = 0;
            remainSeconds = (int)(startDate.getTime()-now.getTime())/1000;
        }
        else if(now.after(endDate)){
            //秒杀已结束
            seckillStatus =  2;
            remainSeconds = -1;
        }
        else if(startDate.before(now)&&endDate.after(now)){
            //秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(seckillStatus);
        return RespBean.success(detailVo);
    }
}
