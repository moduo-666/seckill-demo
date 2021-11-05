package com.moduo.seckill.exception;

import com.moduo.seckill.vo.RespBean;
import org.apache.ibatis.binding.BindingException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Wu Zicong
 * @create 2021-09-23 10:09
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public RespBean handler(Exception e){
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException) e;
            return RespBean.info(500,ex.getMessage());
        }else if(e instanceof BindException){
            BindException ex = (BindException) e;
            return RespBean.info(400,"参数校验异常："+ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        }
        e.printStackTrace();
        return RespBean.info(500,"服务器异常 ："+ e.getMessage());
    }
}
