package com.moduo.seckill.config;

import com.moduo.seckill.common.interceptor.AccessHandlerInterceptor;
import com.moduo.seckill.config.filter.CookieFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Wu Zicong
 * @create 2021-10-14 14:45
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserArgumentResolver userArgumentResolver;
    @Autowired
    private AccessHandlerInterceptor accessHandlerInterceptor;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //        addResourceHandler是指你想在url请求的路径
        // addResourceLocations是静态资源的真实路径
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
    }
        @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessHandlerInterceptor);
//                .excludePathPatterns("/login/**")
//                .excludePathPatterns("/bootstrap/**",
//                                     "/img/**",
//                                     "/jquery-validation/**",
//                                     "/js/**",
//                                     "/layer/**");
    }
    /**
     * 把过滤器注入spring容器
     * @return
     */
//    @Bean
    public FilterRegistrationBean<CookieFilter> jwtFilter() {
        FilterRegistrationBean<CookieFilter> registrationBean = new FilterRegistrationBean<>();
        CookieFilter filter = new CookieFilter();
        registrationBean.setFilter(filter);
        return registrationBean;
    }
}
