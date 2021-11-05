package com.moduo.seckill.controller;

import com.moduo.seckill.pojo.User;
import com.moduo.seckill.rabbitmq.MQSender;
import com.moduo.seckill.service.DemoService;
import com.moduo.seckill.vo.RespBean;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author Wu Zicong
 * @create 2021-05-22 18:27
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private DemoService demoService;
    @Autowired
    private MQSender mqSender;
    @RequestMapping("/hello")
    public String hello(Model model){
        User user = new User(null, "moduo", "123456", "salt", "head", new Date(), new Date(), 1);
        demoService.save(user);
        model.addAttribute("name",user);
        return "hello";
    }
    @RequestMapping("/userInfo")
    @ResponseBody
    public RespBean userInfo(User user){
        return RespBean.success(user);
    }
    @RequestMapping("/mq")
    @ResponseBody
    public void rabbitMQTest(){
        mqSender.send("rabbitMQ测试");
    }
}
