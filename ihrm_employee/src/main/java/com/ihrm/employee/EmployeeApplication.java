package com.ihrm.employee;

import com.ihrm.common.utlis.IdWorker;
import com.ihrm.common.utlis.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName
 * @Description
 * @Author He.Wang
 * @Date 2020/6/13 0:12
 * @Version 1.0
 **/
//1.配置springboot的包扫描
@SpringBootApplication(scanBasePackages = "com.ihrm")
//2.配置jpa注解的扫描
@EntityScan(value="com.ihrm.domain.employee")
//3.注册到Eureka组件
@EnableEurekaClient
public class EmployeeApplication {
    /**
     * 启动方法
     */
    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class,args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}