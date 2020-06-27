package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.poi.ExcelImportUtil;
import com.ihrm.common.utlis.JwtUtils;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.client.DepartmentFeignClient;
import com.ihrm.system.serivce.PermissionService;
import com.ihrm.system.serivce.UserService;
import com.ihrm.system.utils.TencentACUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName
 * @Description
 * @Author He.Wang
 * @Date 2020/6/7 17:22
 * @Version 1.0
 **/


//1.解决跨域问题
@CrossOrigin(origins="*")
//2.设置父路径
@RequestMapping(value = "/sys")
//3.声明RestController
@RestController
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    @Autowired
    private TencentACUtil tencentACUtil;

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * 文件上传
     * @param id
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "user/upload/{id}")
    public Result upload(@PathVariable(value = "id") String id , @RequestParam(name = "file") MultipartFile file) throws IOException {
        //1.调用service保存图片
        String imgUrl = userService.uploadImage(id,file);
        //返回数据
        return new Result(ResultCode.SUCCESS,imgUrl);
    }

    /**
     * 导入excel，添加用户
     */
    @PostMapping(value = "/user/import")
    public Result importUser(@RequestParam(name = "file") MultipartFile file)throws Exception{
        List<User> list = new ExcelImportUtil(User.class).readExcel(file.getInputStream(), 1, 1);
        //3.批量保存用户
        userService.saveAll(list,companyId,companyName);
        return new Result(ResultCode.SUCCESS);
    }

    public static Object getCellValue(Cell cell){
        //1.获取单元格类型
        CellType cellType = cell.getCellType();
        //2.根据单元格数据类型获取数据
        Object value = null;
        switch (cellType){
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                //2种格式 一种数字 一种日期 ，我们现在判断是数字还是日期
                if (DateUtil.isCellDateFormatted(cell)){
                    value = cell.getDateCellValue();
                }else{
                    value = cell.getNumericCellValue();
                }
                break;
            case FORMULA: //代表公式
                value = cell.getCellFormula();
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * 测试Feign组件
     * 调用系统微服务的/test接口传递部门id，通过Feign调用部门微服务获取部门信息
     */
    @GetMapping(value = "/test/{id}")
    public Result testFeign(@PathVariable(value = "id") String id){
        Result result = departmentFeignClient.findById(id);
        return result;
    }

    /**
     * 分配角色
     */
    @PutMapping(value = "/user/assignRoles")
    public Result save(@RequestBody Map<String,Object> map){
        //1.获取到未分配的用户id
        String userId =(String) map.get("id");
        //2.获取到角色的id集合
        String roleId = "roleIds";
        List<String> roleIds = (List<String>)map.get(roleId);
        //3.调用service完成角色分配
        userService.assignRoles(userId,roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 保存
     */
    @PostMapping(value = "/user")
    public Result save(@RequestBody User user){
        //1.设置保存的企业id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //2.调用service保存企业
        userService.save(user);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的用户列表
     */
    @GetMapping(value = "/user")
    public Result findAll(int page , int size, @RequestParam()Map map){
        //1.获取当前的企业Id
        map.put("companyId",companyId);
        //2.完成查询
        Page<User> userPage = userService.findAll(map, page, size);
        //3.构造返回结果
        PageResult pageResult = new PageResult(userPage.getTotalElements(),userPage.getContent());

        return  new Result(ResultCode.SUCCESS,pageResult);
    }

    /**
     * 根据id查询用户
     */
    @GetMapping(value = "/user/{id}")
    public Result findById(@PathVariable(value = "id") String id){
        //添加 roleIds (用户已经具有的角色id数组)
        User user = userService.findById(id);
        UserResult userResult = new UserResult(user);
        return  new Result(ResultCode.SUCCESS,userResult);
    }

    /**
     * 修改部门
     */
    @PutMapping(value = "/user/{id}")
    public Result update(@PathVariable(value = "id") String id,@RequestBody User user){
        //1.设置修改的部门id
        user.setId(id);
        //调用service更新
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequiresPermissions(value = "API-USER-DELETE")
    @DeleteMapping(value = "/user/{id}", name = "API-USER-DELETE")
    public Result delete(@PathVariable(value = "id") String id){
            userService.delete(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 用户注册
     * 1.判断手机号是否存在
     * 2.判断手机号和密码格式是否正确
     * 3.判断验证码是否正确
     * 4.给密码加密
     * 5.创建用户
     */
    @PostMapping(value = "/register")
    public Result register(@RequestBody Map<String,String> userMap){
        String mobile = userMap.get("mobile");
        User byMobile = userService.findByMobile(mobile);
        if(byMobile != null){
            return  new Result(ResultCode.MOBILEAIREADY);
        }
        String password = userMap.get("password");
        if (!mobile.matches("[\\da-zA-Z]+")) {
            return  new Result(ResultCode.MOBILEPASSWORDFORMATERROR);
        }
        if (!password.matches("[\\da-zA-Z]+")) {
            return  new Result(ResultCode.MOBILEPASSWORDFORMATERROR);
        }
      try{
          String autoCode =(String)redisTemplate.opsForValue().get(mobile);
          String code = userMap.get("verificationCode");
          if (autoCode != code && !autoCode.equals(code)){
              return new Result(ResultCode.AUTOCODEERROR);
          }
          password = new Md5Hash(password, mobile, 3).toString();
          userService.register(userMap,password,mobile);
          return new Result(ResultCode.SUCCESS);
      }catch (Exception e){
          return  new Result(ResultCode.MOBILEORPASSWORDERROR);
      }
    }

    /**
     * 根据手机号发送验证码
     */
    @PostMapping(value = "/authcode")
    public Result authCode(@RequestBody String mobile){

        mobile = mobile.replace("=", "");
        User byMobile = userService.findByMobile(mobile);
        if(byMobile != null){
            return  new Result(ResultCode.MOBILEAIREADY);
        }
        tencentACUtil.sendMsgByTxPlatform(mobile);
        return new Result(ResultCode.SUCCESS);
    }


    /**
     * 用户登录
     *  1.通过service根据mobile查询用户
     *  2.比较password
     *  3.生成jwt信息
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody Map<String,String> loginMap){
        String mobile = loginMap.get("mobile");
        String password = loginMap.get("password");
        if (!mobile.matches("[\\da-zA-Z]+")) {
            return  new Result(ResultCode.MOBILEPASSWORDFORMATERROR);
        }
        if (!password.matches("[\\da-zA-Z]+")) {
            return  new Result(ResultCode.MOBILEPASSWORDFORMATERROR);
        }
        //Shiro的登录步骤
        try {
            //1.构造登录的令牌 UserNamePasswordToken
            //加密密码  三个参数: 密码，盐，加密次数
            password = new Md5Hash(password, mobile, 3).toString();
            UsernamePasswordToken upToken = new UsernamePasswordToken(mobile, password);
            //2.获取subject
            Subject subject = SecurityUtils.getSubject();
            //3.调用login方法,进入Realm完成用户认证
            subject.login(upToken);
            //4.获取sessionId
             String sessionId =(String) subject.getSession().getId();
            //5.构造返回结果
            return new Result(ResultCode.SUCCESS,sessionId);
        }catch (Exception e){
            return  new Result(ResultCode.MOBILEORPASSWORDERROR);
        }

        //jwt的登录步骤
//        User user = userService.findByMobile(mobile);
//        if (user == null || !user.getPassword().equals(password)){
//            //登录失败
//            return  new Result(ResultCode.MOBILEORPASSWORDERROR);
//        }else{
//            //登录成功
//            //api权限字符串
//            StringBuilder sb = new StringBuilder();
//            //获取到所有的可访问api权限
//            for (Role role : user.getRoles()) {
//                for (Permission perm : role.getPermissions()) {
//                    if (perm.getType() == PermissionConstants.PY_API){
//                        sb.append(perm.getCode()).append(",");
//                    }
//                }
//            }
//            Map<String,Object> map = new HashMap<>();
//            map.put("apis",sb.toString());
//            map.put("companyId",user.getCompanyId());
//            map.put("companyName",user.getCompanyName());
//            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
//            return new Result(ResultCode.SUCCESS,token);
//        }
    }

    /**
     * 用户成功之后获取用户信息
     *  1.获取用户id
     *  2.根据用户id查询用户
     *  3.构建返回值对象
     *  4.响应
     */
    @PostMapping(value = "/profile")
    public Result profile(HttpServletRequest request) throws Exception{
        //获取到 session 中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.通过subject获取所有安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        //2.获取安全数据
        ProfileResult result =(ProfileResult) principals.getPrimaryPrincipal();


//        String userId = claims.getId();
//        User user = userService.findById(userId);
//        //根据不同的用户级别获取用户权限
//
//
//        ProfileResult result = null;
//        if ("user".equals(user.getLevel())){
//            result = new ProfileResult(user);
//        }else{
//            Map map = new HashMap();
//            if("coAdmin".equals(user.getLevel())){
//                map.put("enVisible","1");
//
//            }
//            List<Permission> list = permissionService.findAll(map);
//            result = new ProfileResult(user,list);
//        }

        return  new Result(ResultCode.SUCCESS,result);
    }
}