package com.moduo.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Wu Zicong
 * @create 2021-10-14 14:10
 */
@Configuration
public class RedisConfig {
    @Bean
     public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //key的序列化
        template.setKeySerializer(new StringRedisSerializer());
        //value的序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //hash类型key序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        //hash类型value序列化
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        //注入连接工厂
        template.setConnectionFactory(factory);
        return template;
    }

    /**
     * 调用脚本
     * @return
     */
    @Bean
    public DefaultRedisScript<Boolean> script(){
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        //放在和application.yml 同层目录下
        redisScript.setLocation(new ClassPathResource("lock.lua"));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }
    @Bean(name = "seckillScript")
    public DefaultRedisScript<Long> seckillScript(){
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //放在和application.yml 同层目录下
        redisScript.setLocation(new ClassPathResource("stock.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
