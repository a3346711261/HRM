package com.ihrm.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ClassName Eureka服务端的启动类
 * @Description
 * @Author He.Wang
 * @Date 2020/6/13 14:50
 * @Version 1.0
 **/
@SpringBootApplication
//开启Eureka服务端的配置
@EnableEurekaServer
public class EurekaServer {

        public static void main(String[]  args)  {
            SpringApplication.run(EurekaServer.class,  args);
         }
}