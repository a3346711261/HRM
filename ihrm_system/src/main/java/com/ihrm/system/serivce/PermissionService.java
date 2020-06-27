package com.ihrm.system.serivce;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.service.BaseService;
import com.ihrm.common.utlis.BeanMapUtils;
import com.ihrm.common.utlis.IdWorker;
import com.ihrm.common.utlis.PermissionConstants;
import com.ihrm.domain.system.*;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName 权限业务层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/8 14:26
 * @Version 1.0
 **/

@Service
@Transactional
public class PermissionService extends BaseService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionMenuDao permissionMenuDao;

    @Autowired
    private PermissionPointDao permissionPointDao;

    @Autowired
    private PermissionApiDao permissionApiDao;


    @Autowired
    private IdWorker idWorker;

    /**
     * 1.保存权限
     */
    public void save(Map<String,Object> map) throws Exception {
        //设置主键的值
        String id = idWorker.nextId() + "";
        //1.通过map构造permission对象
        Permission permission =BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(id);
        //2.根据类型构造不同的资源对象(菜单，按钮，api)
        int type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                menu.setId(id);
                permissionMenuDao.save(menu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                point.setId(id);
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map, PermissionApi.class);
                api.setId(id);
                permissionApiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        //3.保存
        permissionDao.save(permission);
    }

    /**
     * 2.更新权限
     */
    public void update(Map<String,Object> map) throws Exception {
        Permission permission =BeanMapUtils.mapToBean(map, Permission.class);
        //1.根据id查询部门
        Permission permission1 = permissionDao.findById(permission.getId()).get();
        permission1.setName(permission.getName());
        permission1.setCode(permission.getCode());
        permission1.setDescription(permission.getDescription());
        permission1.setEnVisible(permission.getEnVisible());
        //2.根据类型构造不同的资源
        //3.查询不同的资源设置修改的属性
        int type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                PermissionMenu menu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
                menu.setId(permission.getId());
                permissionMenuDao.save(menu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map, PermissionPoint.class);
                point.setId(permission.getId());
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map, PermissionApi.class);
                api.setId(permission.getId());
                permissionApiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        //4.更新权限，更新资源
        permissionDao.save(permission);
    }

    /**
     * 3.根据id查询权限
     *   1.查询权限
     *   2.根据权限的类型查询资源
     *   3.查询map集合
     */
    public Map<String, Object> findById(String id){
        Permission permission = permissionDao.findById(id).get();
        int type = permission.getType();

        Object object = null;

        if(type == PermissionConstants.PY_MENU){
            object = permissionMenuDao.findById(id).get();
        }else if(type == PermissionConstants.PY_POINT){
            object = permissionPointDao.findById(id).get();
        }else if(type == PermissionConstants.PY_API){
            object = permissionApiDao.findById(id).get();
        }else{
            throw  new  CommonException(ResultCode.FAIL);
        }

        Map<String, Object> map = BeanMapUtils.beanToMap(object);
        map.put("name",permission.getName());
        map.put("type",permission.getType());
        map.put("code",permission.getCode());
        map.put("description",permission.getDescription());
        map.put("pid",permission.getPid());
        map.put("enVisible",permission.getEnVisible());
        return map;
    }

    /**
     * 4.查询全部
     *  type  查询全部权限列表type: 0 ;菜单 + 按钮(权限点) 1:菜单 2:按钮 (权限点) 3:API接口
     *  enVisible : 0;查询所以saas平台的最高权限，1；查询企业的权限
     *  pid : 父id
     */
    public List<Permission> findAll(Map<String,Object> map){
        //1.需要查询条件
        Specification<Permission> spec = new Specification<Permission>() {
            /**
             * 动态查询条件
             * @param root
             * @param cq
             * @param cb
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                //根据父id查询
                if(!StringUtils.isEmpty(map.get("pid"))){
                    list.add(cb.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                //根据enVisible查询
                if(!StringUtils.isEmpty(map.get("enVisible"))){
                    list.add(cb.equal(root.get("enVisible").as(String.class),(String)map.get("enVisible")));
                }
                //根据类型 type 查询
                if(!StringUtils.isEmpty(map.get("type"))){
                    String ty = (String) map.get("type");
                    CriteriaBuilder.In<Object> in = cb.in(root.get("type"));
                    if ("0".equals(ty)){
                        in.value(1).value(2);
                    }else{
                        in.value(Integer.parseInt(ty));
                    }
                }


                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };


        return permissionDao.findAll(spec);
    }

    /**
     * 5.根据id删除用户
     */
    public void delete(String id){
        //1.根据id查询部门
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        //2.根据类型构造不同的资源
        int type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }

}