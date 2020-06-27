package com.ihrm.company.controller;

import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName 公司控制层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/5 12:05
 * @Version 1.0
 **/
@CrossOrigin(origins="*")
@RestController
@RequestMapping(value = "/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    //保存企业

    @PostMapping
    public Result save(@RequestBody Company company){
        companyService.add(company);
        return new Result(ResultCode.SUCCESS);
    }


    //根据id更新企业

    /**
     *
     * @param id
     * @param company
     * @return
     *
     * 1.方法
     * 2.请求参数
     * 3.响应
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(value = "id") String id ,@RequestBody Company company){
        company.setId(id);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id删除企业

    @DeleteMapping(value = "/{id}")
    public Result deleteById(@PathVariable(value = "id") String id){
        companyService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    //根据id查询企业

    @GetMapping(value = "{id}")
    public Result findById(@PathVariable(value = "id") String id){
        Company company = companyService.findById(id);
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(company);
        return result;
    }

    //查询全部企业
    @GetMapping
    public Result findAll(){
        List<Company> companies = companyService.findAll();
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(companies);
        return result;
    }


}