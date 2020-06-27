package com.ihrm.common.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @ClassName Feign拦截器添加请求头
 * @Description
 * @Author He.Wang
 * @Date 2020/6/13 16:00
 * @Version 1.0
 **/

@Configuration
public class FeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            //获取所有浏览器发送的请求属性: 获取请求头赋值到Feign
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 获取请求属性
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                // 判断属性是否为空
                if (attributes != null){
                    //获取到第一次发送请求的对象
                    HttpServletRequest request = attributes.getRequest();
                    //获取浏览器发起的请求头
                    Enumeration<String> headerNames = request.getHeaderNames();
                    //判断请求头不等于null
                    if(headerNames != null){
                        while (headerNames.hasMoreElements()){
                            //请求头名称 Authorization
                            String name = headerNames.nextElement();
                            //请求头数据 Bearer df95d070-2bcd-4b76-8c6d-a16bfdafe41d
                            String value = request.getHeader(name);
                            //将头信息赋值到 requestTemplate中
                            requestTemplate.header(name,value);
                        }
                    }
                }
            }
        };
    }
}