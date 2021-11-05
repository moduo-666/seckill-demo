package com.moduo.seckill.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解
 * @author Wu Zicong
 * @create 2021-11-03 15:54
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit{
    /*暂定请求时间*/
    int second();
    /* 限流次数*/
    int maxCount();
    /*是否需要登录*/
    boolean needLogin() default true;
}
