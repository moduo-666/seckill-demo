package com.moduo.seckill.util;

import java.util.UUID;

/**
 * uuid工具类
 * @author Wu Zicong
 * @create 2021-10-14 10:25
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
