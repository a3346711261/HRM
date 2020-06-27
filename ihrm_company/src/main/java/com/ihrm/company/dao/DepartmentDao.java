package com.ihrm.company.dao;

import com.ihrm.domain.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName 部门Dao层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/6 23:15
 * @Version 1.0
 **/
public interface DepartmentDao extends JpaRepository<Department,String> , JpaSpecificationExecutor<Department> {

    Department findByCodeAndCompanyId(String code, String companyId);
}
