package com.ihrm.common.exception;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName 。。
 * @Description
 * @Author He.Wang
 * @Date 2020/6/6 18:36
 * @Version 1.0
 **/
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    public Result error(HttpServletRequest request, HttpServletResponse response,AuthorizationException e) {
        return new Result(ResultCode.UNAUTHORISE);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result error(HttpServletRequest request, HttpServletResponse response,
                        Exception e) throws IOException {
        e.printStackTrace();
        if (e.getClass() == CommonException.class) {
            CommonException ce = (CommonException) e;
            return new Result(ce.getCode());
        } else {
            return Result.ERROR();
        }
    }
}