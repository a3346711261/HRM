package com.ihrm.system.serivce;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utlis.IdWorker;
import com.ihrm.common.utlis.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @ClassName 角色业务层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/8 0:37
 * @Version 1.0
 **/
@Service
public class RoleService extends BaseService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 分配权限
     */
    public void assignPerm(String roleId, List<String> permIds){
        //1.获取分配角色的列表
        Role role = roleDao.findById(roleId).get();
        //2.构造角色的权限集合
        Set<Permission> permissionSet = new HashSet<>();
        for (String permId : permIds) {
            Permission permission = permissionDao.findById(permId).get();
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PY_API, permission.getId());
            permissionSet.addAll(apiList);  //自定赋予API权限
            permissionSet.add(permission);  //当前菜单或按钮的权限
        }
        //3.设置角色和权限的关系
        role.setPermissions(permissionSet);
        //4.更新角色
        roleDao.save(role);

    }

    /**
     * 1.保存角色
     */
    public void save(Role role){
        //设置主键的值
        String s = idWorker.nextId() + "";
        role.setId(s);
        //调用dao保存部门
        roleDao.save(role);
    }

    /**
     * 2.更新用户
     */
    public void update(Role role){
        //1.根据id查询部门
        Role role1 = roleDao.findById(role.getId()).get();
        //2.设置部门属性
        role1.setName(role.getName());
        role1.setDescription(role.getDescription());
        //3.更新部门
        roleDao.save(role1);
    }

    /**
     * 3.根据id查询用户
     */
    public Role findById(String id){

        return roleDao.findById(id).get();
    }

    /**
     * 4.查询部门用户
     *      参数： map集合
     *      hasDept
     *      departmentId
     *      companyId
     */
    public Page<Role> findAll(String companyId , int page , int size){
        //1.需要查询条件
        Specification<Role> spec = getSpec(companyId);
        Page<Role> rolePage = roleDao.findAll(spec, new PageRequest(page - 1, size));
        return rolePage;
    }

    /**
     * 5.根据id删除用户
     */
    public void delete(String id){

        roleDao.deleteById(id);
    }

    /**
     * 查询所有角色:
     *      根据内部维护的公司id进行查询该公司的所有角色
     */
    public List<Role> findAll(String companyId){
        return roleDao.findAll(getSpec(companyId));
    }
}