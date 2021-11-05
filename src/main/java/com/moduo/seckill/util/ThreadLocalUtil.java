package com.moduo.seckill.util;

import com.moduo.seckill.pojo.User;

/**
 * @author Wu Zicong
 * @create 2021-10-29 15:02
 */
public class ThreadLocalUtil {
    private final static ThreadLocal<User> userThread = new ThreadLocal<>();
    public static void setUser(User user){
        userThread.set(user);
    }
    public static User getUser(){
       return userThread.get();
    }
    public static Long getUserId(){
        User user = getUser();
        if(user != null){
            return getUser().getId();
        }
        return null;
    }
    public static void remove(){
        userThread.remove();
    }
}
