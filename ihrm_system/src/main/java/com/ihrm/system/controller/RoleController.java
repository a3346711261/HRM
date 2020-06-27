package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.response.RoleResult;
import com.ihrm.system.serivce.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @ClassName 角色控制层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/8 0:46
 * @Version 1.0
 **/


//1.解决跨域问题
@CrossOrigin(origins="*")
//2.设置父路径
@RequestMapping(value = "/sys")
//3.声明RestController
@RestController
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 分配角色
     */
    @PutMapping(value = "/role/assignPrem")
    public Result save(@RequestBody Map<String,Object> map){
        //1.获取到未分配的用户id
        String roleId =(String) map.get("id");
        //2.获取到角色的id集合
        String permId = "permIds";
        List<String> permIds = (List<String>)map.get(permId);
        //3.调用service完成角色分配
        roleService.assignPerm(roleId,permIds);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 保存
     */
    @PostMapping(value = "/role")
    public Result save(@RequestBody Role role){
        //1.设置保存的企业id
        role.setCompanyId(companyId);
        //2.调用service保存企业
        roleService.save(role);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的部门列表
     */
    @GetMapping(value = "/role")
    public Result findAll(int page ,int size){
        //2.完成查询
        Page<Role> rolePage = roleService.findAll(companyId, page, size);
        //3.构造返回结果
        PageResult pageResult = new PageResult(rolePage.getTotalElements(),rolePage.getContent());
        return  new Result(ResultCode.SUCCESS,pageResult);
    }

    /**
     * 根据id查询部门
     */
    @GetMapping(value = "/role/{id}")
    public Result findById(@PathVariable(value = "id") String id){
        Role role = roleService.findById(id);
        RoleResult roleResult = new RoleResult(role);
        return  new Result(ResultCode.SUCCESS,roleResult);
    }

    /**
     * 修改部门
     */
    @PutMapping(value = "/role/{id}")
    public Result update(@PathVariable(value = "id") String id,@RequestBody Role role){
        //1.设置修改的部门id
        role.setId(id);
        //调用service更新
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }

    @DeleteMapping(value = "/role/{id}")
    public Result delete(@PathVariable(value = "id") String id){
        roleService.delete(id);
        return new Result(ResultCode.SUCCESS);
    }

    @GetMapping(value="/role/list" )
    public Result findAll() throws Exception {
        List<Role> roleList = roleService.findAll(companyId);
        return new Result(ResultCode.SUCCESS,roleList);
    }
}