package com.ihrm.common.shiro.session;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @ClassName 自定义会话管理器
 * @Description
 * @Author He.Wang
 * @Date 2020/6/12 15:45
 * @Version 1.0
 **/

public class CustomSessionManager extends DefaultWebSessionManager {
    /**
     * 头信息中具有 sessionId
     *
     *      请求头: Authorization sessionId
     *
     * 指定 sessionId 的获取方式
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //获取请求头中的数据
        String id = WebUtils.toHttp(request).getHeader("Authorization");
        if (StringUtils.isEmpty(id)){
            //如果没有携带请求头，生成新的 sessionId
            return super.getSessionId(request,response);
        }else{
            //请求头信息: Bearer sessionId
            id = id.replaceAll("Bearer ","");
            //返回 sessionId;
            //配置你这个 sessionId 是那获取到的 ，第二个参数表示请求头中获取到的 header
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
            //配置 sessionId 具体是什么，具体叫什么，就是第二个参数 id
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            //配置 sessionId 要不要验证， true 就行
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }
    }



}