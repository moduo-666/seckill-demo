package com.moduo.seckill.util;

import com.moduo.seckill.pojo.User;
import com.moduo.seckill.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Wu Zicong
 * @create 2021-10-18 13:59
 */
@Component
public class SecurityUtil {
    private User user;
    public User getUser(){
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
