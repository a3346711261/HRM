package com.ihrm.company;

/**
 * @ClassName 启动类
 * @Description
 * @Author He.Wang
 * @Date 2020/6/5 3:22
 * @Version 1.0
 **/

import cn.yukonga.yrpc.core.annotation.EnableYRpc;
import com.ihrm.common.utlis.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * 1.配置spring boot 的包扫描
 * @SpringBootApplication(scanBasePackages = "com.ihrm.company")
 * 2.配置jpa注解扫描
 * @EntityScan(value = "com.ihrm.domain.company")
 */
@SpringBootApplication(scanBasePackages = "com.ihrm")
@EntityScan(value = "com.ihrm.domain.company")
//注册到Eureka
@EnableEurekaClient
public class CompanyApplication {

    /**
     * 启动方法
     */
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}