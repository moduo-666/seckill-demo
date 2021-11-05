package com.moduo.seckill.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moduo.seckill.mapper.UserMapper;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.service.IUserService;
import com.moduo.seckill.util.CookieUtil;
import com.moduo.seckill.util.MD5Util;
import com.moduo.seckill.util.UUIDUtil;
import com.moduo.seckill.util.ValidatorUtil;
import com.moduo.seckill.vo.LoginVo;
import com.moduo.seckill.vo.RespBean;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wuzicong
 * @since 2021-09-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        User user = userMapper.selectById(mobile);
        if(user!=null){
            String s = MD5Util.inputPassToDBPass(password, user.getSlat());
            if(!s.equals(user.getPassword())){
                return  RespBean.info(400,"登录失败,用户名或密码不正确");
            }
            //生成cookie
            String ticket = UUIDUtil.uuid();
            //将用户信息存入redis中
            redisTemplate.opsForValue().set("user:"+ticket, user);
//            request.getSession().setAttribute(ticket,user);
            CookieUtil.setCookie(request,response,"userTicket",ticket);
            return RespBean.info(200,"登录成功",ticket);
        }
        return  RespBean.info(400,"登录失败,用户名或密码不正确","登录失败,用户名或密码不正确");
    }

    @Override
    public User getUserByCookie(String ticket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(ticket))
            return null;
        User user = (User)redisTemplate.opsForValue().get("user:" + ticket);
        if(user != null){
            CookieUtil.setCookie(request,response,"userTicket",ticket);
        }
        return user;
    }

    @Override
    public String login() {
        return "login";
    }

    @Override
    public RespBean updatePassword(String userTicket, String password,HttpServletRequest request,HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if(user == null){
            return RespBean.info(400,"失败，请刷新页面后重试");
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSlat()));
        int result = baseMapper.updateById(user);
        if(result == 1){
            //删除redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.info(200,"更新密码成功");
        }
        return RespBean.info(400,"更新密码失败");
    }
}
