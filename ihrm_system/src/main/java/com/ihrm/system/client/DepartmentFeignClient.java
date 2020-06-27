package com.ihrm.system.client;

import com.ihrm.common.entity.Result;
import com.ihrm.domain.company.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName 声明接口，通过Feign调用其他微服务
 * @Description
 * @Author He.Wang
 * @Date 2020/6/13 15:28
 * @Version 1.0
 **/

//声明调用那个微服务的名称
@FeignClient("ihrm-company")
public interface DepartmentFeignClient {

    /**
     * 调用微服务的接口
     */
    @GetMapping(value = "/company/department/{id}")
     Result findById(@PathVariable(value = "id") String id);

    @PostMapping(value = "/company/department/search")
     Department findByCode(@RequestParam(value = "code") String code , @RequestParam(value = "companyId") String companyId);
}
