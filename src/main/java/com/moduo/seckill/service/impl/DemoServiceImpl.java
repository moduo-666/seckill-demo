package com.moduo.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moduo.seckill.mapper.DemoMapper;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * @author Wu Zicong
 * @create 2021-09-22 9:32
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, User>implements DemoService{

}
