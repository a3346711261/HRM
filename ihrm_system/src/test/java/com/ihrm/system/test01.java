package com.ihrm.system;

import com.ihrm.system.serivce.UserService;
import com.ihrm.system.utils.TencentACUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName
 * @Description
 * @Author He.Wang
 * @Date 2020/6/24 18:31
 * @Version 1.0
 **/

@SpringBootTest
@RunWith(SpringRunner.class)
public class test01 {

    @Autowired
    private TencentACUtil tencentACUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        tencentACUtil.sendMsgByTxPlatform("17677205017");
    }

    @Test
    public void test1() throws Exception {

        String autoCode =(String)redisTemplate.opsForValue().get("17677205017");
        System.out.println(autoCode);

    }

    @Autowired
    UserService userService;


}