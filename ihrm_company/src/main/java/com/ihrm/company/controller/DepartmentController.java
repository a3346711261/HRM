package com.ihrm.company.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName 部门控制层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/6 23:25
 * @Version 1.0
 **/

//1.解决跨域问题
@CrossOrigin(origins="*")
//2.设置父路径
@RequestMapping(value = "/company")
//3.声明RestController
@RestController
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;

    /**
     * 保存
     * @return
     */
    @PostMapping(value = "/department")
    public Result save(@RequestBody Department department){
        //1.设置保存的企业id
        /**
         * 企业id:目前使用固定的1
         */
        department.setCompanyId(companyId);
        //2.调用service保存企业
        departmentService.save(department);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的部门列表
     */
    @GetMapping(value = "/department")
    public Result findAll(){
        //1.指定企业id
        Company company = companyService.findById(companyId);
        //2.完成查询
        List<Department> list = departmentService.findAll(companyId);
        //3.构造返回结果
        DeptListResult deptListResult = new DeptListResult(company,list);
        return  new Result(ResultCode.SUCCESS,deptListResult);
    }

    /**
     * 根据id查询部门
     */
    @GetMapping(value = "/department/{id}")
    public Result findById(@PathVariable(value = "id") String id){
        Department department = departmentService.findById(id);
        return  new Result(ResultCode.SUCCESS,department);
    }

    /**
     * 修改部门
     */
    @PutMapping(value = "/department/{id}")
    public Result update(@PathVariable(value = "id") String id,@RequestBody Department department){
        //1.设置修改的部门id
        department.setId(id);
        //调用service更新
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping(value = "/department/{id}")
    public Result delete(@PathVariable(value = "id") String id){
        departmentService.delete(id);
        return new Result(ResultCode.SUCCESS);
    }

    @PostMapping(value = "/department/search")
    public Department findByCode(@RequestParam(value = "code") String code ,@RequestParam(value = "companyId") String companyId){
       Department department = departmentService.findByCode(code,companyId);
       return department;
    }

}