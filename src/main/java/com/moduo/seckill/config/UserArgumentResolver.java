package com.moduo.seckill.config;

import com.moduo.seckill.pojo.User;
import com.moduo.seckill.util.ThreadLocalUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * supportsParameter：用于判定是否需要处理该参数分解，返回true为需要，
 *                    并会去调用下面的方法resolveArgument。
 * resolveArgument：真正用于处理参数分解的方法，返回的Object就是controller方法上的形参对象。
 *
 * @author Wu Zicong
 * @create 2021-10-14 14:47
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> classz = methodParameter.getParameterType();
        return classz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        return ThreadLocalUtil.getUser();
    }
}
