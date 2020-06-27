package com.ihrm.system.dao;

import com.ihrm.domain.company.Company;
import com.ihrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName 用户Dao层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/7 16:14
 * @Version 1.0
 **/

public interface UserDao extends JpaRepository<User,String> , JpaSpecificationExecutor<User> {

    public User findByMobile(String mobile);

}