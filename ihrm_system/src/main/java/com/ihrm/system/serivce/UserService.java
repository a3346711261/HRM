package com.ihrm.system.serivce;

import cn.yukonga.yrpc.core.annotation.RemoteReference;
import com.ihrm.common.service.BaseService;
import com.ihrm.common.utlis.IdWorker;
import com.ihrm.common.utlis.QiniuUploadUtil;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.client.DepartmentFeignClient;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import com.ihrm.system.utils.BaiduAiUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName 用户业务层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/7 16:16
 * @Version 1.0
 **/

@Service
public class UserService extends BaseService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    @Autowired
    public BaiduAiUtil baiduAiUtil;


    /**
     * 批量用户保存
     */
    @Transactional
    public void saveAll(List<User> list,String companyId ,String companyName){
        for (User user : list) {
            //默认密码
            user.setPassword(new Md5Hash("123456",user.getMobile(),3).toString());
            //设置id
            user.setId(idWorker.nextId() + "");
            //基本属性
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setInServiceStatus(1);
            user.setEnableState(1);
            user.setLevel("user");
            Department department = departmentFeignClient.findByCode(user.getDepartmentId(), companyId);
            if(department != null){
                user.setDepartmentId(department.getId());
                user.setDepartmentName(department.getName());
            }
            userDao.save(user);
        }
    }


    /**
     * 根据mobile查询用户
     */
    public User findByMobile(String mobile){
        return userDao.findByMobile(mobile);
    }


    /**
     * 1.保存用户
     */
    public void save(User user){
        //设置主键的值
        String s = idWorker.nextId() + "";
        String password = new Md5Hash("123456", user.getMobile(), 3).toString();
        //设置初始密码
        user.setLevel("user");
        user.setPassword(password);
        user.setEnableState(1);
        user.setId(s);
        //调用dao保存部门
        userDao.save(user);
    }

    /**
     * 2.更新用户
     */
    public void update(User user){
        //1.根据id查询部门
        User user1 = userDao.findById(user.getId()).get();
        //2.设置部门属性
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
        user1.setDepartmentId(user.getDepartmentId());
        user1.setDepartmentName(user.getDepartmentName());
        //3.更新部门
        userDao.save(user1);
    }

    /**
     * 3.根据id查询用户
     */
    public User findById(String id){
        Optional<User> user = userDao.findById(id);
        if (user != null && user.isPresent()){
          return user.get();
        }
        return null;
    }

    /**
     * 4.查询部门用户
     *      参数： map集合
     *      hasDept
     *      departmentId
     *      companyId
     */
    public Page<User> findAll(Map<String,Object> map ,int page ,int size){
        //1.需要查询条件
        Specification<User> spec = new Specification<User>() {
            /**
             * 动态查询条件
             * @param root
             * @param cq
             * @param cb
             * @return
             */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的公司companyId 是否为空构造查询条件
                if(!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(cb.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                //根据请求的部门departmentId 是否为空构造查询条件
                if(!StringUtils.isEmpty(map.get("departmentId"))){
                    list.add(cb.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                //暂时用不到
                if(!StringUtils.isEmpty(map.get("hasDept"))){
                    //根据请求的hasDept判断 是否分配部门 0 未分配 (departmentId = null) ，1已分配 (departmentId != null)
                    if("0".equals((String)map.get("hasDept"))){
                        list.add(cb.isNull(root.get("departmentId")));
                    }else{
                        list.add(cb.isNotNull(root.get("departmentId")));
                    }
                }


                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };
        //2.分页
        Page<User> userPage = userDao.findAll(spec, new PageRequest(page-1, size));

        return userPage;
    }

    /**
     * 5.根据id删除用户
     */
    public void delete(String id){

        userDao.deleteById(id);
    }

    /**
     * 分配角色
     */
    public void assignRoles(String userid , List<String> roleIds){
        //1.根据id查询
        User user = userDao.findById(userid).get();
        //2.设置用户的角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //3.更新用户
        user.setRoles(roles);
        userDao.save(user);
    }

    /**
     * 完成图片处理  DataUrl的方式存储
     * @param id       用户id
     * @param file      用户上传头像的路径
     * @return      请求路径
     */
//    public String uploadImage(String id, MultipartFile file) throws IOException {
//        //1.根据id查询用户
//        User user = userDao.findById(id).get();
//        //2.使用DataURL的形式处理图片(对图片byte数组进行base64编码)
//        String encode = "data:image/png;base64,"+Base64.encode(file.getBytes());
//        //3.更新用户头像地址
//        user.setStaffPhoto(encode);
//        userDao.save(user);
//        //4.返回
//        return encode;
//    }



    /**
     * 完成图片处理  七牛云的方式存储
     *   注册到百度AI人脸库中
     *          1.调用百度云接口，判断当前用户是否已注册面部信息
     *          2.已注册，更新
     *          3.未注册，注册
     */
    public String uploadImage(String id, MultipartFile file) throws IOException {
        //1.根据id查询用户
        User user = userDao.findById(id).get();
        //2.将图片上传到七牛云服务存储,获取到请求路径
        String imgUrl = new QiniuUploadUtil().upload(user.getId(),file.getBytes());  //上传图片名称
        //3.更新用户头像地址
        user.setStaffPhoto(imgUrl);
        userDao.save(user);

        //判断是否已经注册面部信息
        Boolean b = baiduAiUtil.faceExis(id);
        String imgBase64 = Base64.encodeBase64String(file.getBytes());
        if(b){
            //更新
            baiduAiUtil.faceUpdate(id,imgBase64);
        }else{
            //注册
            baiduAiUtil.faceRegister(id,imgBase64);
        }
        //4.返回
        return imgUrl;
    }

    public void register(Map<String, String> userMap, String password,String moblie) {
        //设置主键的值
        String uId = idWorker.nextId() + "";
        User user = new User();
        user.setCompanyId("1");
        user.setCompanyName("小强联盟有限集团");
        user.setMobile(moblie);
        user.setPassword(password);
        user.setUsername(userMap.get("username"));
        user.setLevel("user");
        user.setId(uId);
        user.setEnableState(1);
        userDao.save(user);
    }

}