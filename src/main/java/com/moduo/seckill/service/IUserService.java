package com.moduo.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.vo.LoginVo;
import com.moduo.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Wuzicong
 * @since 2021-09-22
 */
public interface IUserService extends IService<User> {
    /**
     * 登录逻辑
     * @param loginVo
     * @return
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据cookie获取用户
     * @param ticket
     * @return
     */
    User getUserByCookie(String ticket, HttpServletRequest request, HttpServletResponse response);

    String login();

    /**
     * 修改密码
     * @param userTicket
     * @param password
     * @return
     */
    RespBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);
}
