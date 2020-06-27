package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Permission;
import com.ihrm.system.serivce.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName 权限控制层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/8 14:21
 * @Version 1.0
 **/

//1.解决跨域问题
@CrossOrigin(origins="*")
//2.设置父路径
@RequestMapping(value = "/sys")
//3.声明RestController
@RestController
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 保存
     */
    @PostMapping(value = "/permission")
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        permissionService.save(map);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 修改
     */
    @PutMapping(value = "/permission/{id}")
    public Result update(@PathVariable(value = "id") String id,@RequestBody Map<String,Object> map) throws Exception {
        map.put("id",id);
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询列表
     */
    @GetMapping(value = "/permission")
    public Result findAll(@RequestParam Map map){
        List<Permission> permissions = permissionService.findAll(map);

        return  new Result(ResultCode.SUCCESS,permissions);
    }

    /**
     * 根据id查询
     */
    @GetMapping(value = "/permission/{id}")
    public Result findById(@PathVariable(value = "id") String id){
        Map map = permissionService.findById(id);
        return  new Result(ResultCode.SUCCESS,map);
    }



    /**
     * 删除
     * @param id
     * @return
     */

    @DeleteMapping(value = "/permission/{id}")
    public Result delete(@PathVariable(value = "id") String id){
        permissionService.delete(id);
        return new Result(ResultCode.SUCCESS);
    }

}