package com.ihrm.common.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName 公共错误跳转接口
 * @Description
 * @Author He.Wang
 * @Date 2020/6/12 16:37
 * @Version 1.0
 **/

@RestController
public class ErrorController {

    @GetMapping(value = "autherror")
    public Result autherror(int code){
        return code == 1? new Result(ResultCode.UNAUTHENTICATED):new Result(ResultCode.UNAUTHORISE);
    }
}