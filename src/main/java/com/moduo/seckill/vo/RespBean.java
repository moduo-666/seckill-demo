package com.moduo.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wu Zicong
 * @create 2021-09-22 14:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private static final long serialVersionUID = 1L;
    private long code;
    private String message;
    private Object obj;
    public static RespBean info(long code,String message,Object obj){
        return new RespBean(code,message,obj);
    }
    public static RespBean info(long code,String message){
        return new RespBean(code,message,null);
    }
    public static RespBean success(){
        return new RespBean(200,"SUCCESS",null);
    }
    public static RespBean success(Object obj){
        return new RespBean(200,"SUCCESS",obj);
    }
    public static RespBean error(){
        return new RespBean(500,"FAILURE",null);
    }
    public static RespBean error(Object obj){
        return new RespBean(500,"FAILURE",obj);
    }
}
