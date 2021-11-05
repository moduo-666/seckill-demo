package com.moduo.seckill.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moduo.seckill.common.annotation.AccessLimit;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.service.IUserService;
import com.moduo.seckill.util.CookieUtil;
import com.moduo.seckill.util.ThreadLocalUtil;
import com.moduo.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author Wu Zicong
 * @create 2021-11-03 17:24
 */
@Component
public class AccessHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }
            int second = accessLimit.second();
            boolean needLogin = accessLimit.needLogin();
            int maxCount = accessLimit.maxCount();
            String key = request.getRequestURI() +":"+ request.getRemoteAddr();
            //需要登录
            if (needLogin) {
                User user = getUser(request, response);
                if (user == null) {
                    // 如果没有登录
                    render(response,"您还没有登录，请先登录");
                    return false;
                }
                key+= ":" + user.getId();
                // 将用户信息注入线程独属的ThreadLocal
                ThreadLocalUtil.setUser(user);
                Long userId = ThreadLocalUtil.getUserId();
            }
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Integer count = (Integer)valueOperations.get(key);
            if(count == null){

                valueOperations.set(key , 1 ,second , TimeUnit.SECONDS);
            }else if(count < maxCount){
                valueOperations.increment(key);
            }else{
                render(response,"请求过于频繁");
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, String s) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        RespBean info = RespBean.info(400, s);
        out.write(new ObjectMapper().writeValueAsString(info));
        out.flush();
        out.close();
    }

    private User getUser(HttpServletRequest request,HttpServletResponse response){
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        return userService.getUserByCookie(ticket, request, response);
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //关闭threadLocal
        if(ThreadLocalUtil.getUser() != null)
        ThreadLocalUtil.remove();
    }
}
