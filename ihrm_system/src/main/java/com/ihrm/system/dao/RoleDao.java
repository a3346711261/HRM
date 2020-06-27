package com.ihrm.system.dao;

import com.ihrm.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName 角色Dao层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/8 0:36
 * @Version 1.0
 **/
public interface RoleDao extends JpaRepository<Role,String> , JpaSpecificationExecutor<Role> {
}
