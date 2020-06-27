package com.ihrm.company.dao;

import com.ihrm.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName Company接口
 * @Description
 * @Author He.Wang
 * @Date 2020/6/5 11:28
 * @Version 1.0
 **/

/**
 * 自定义接口继承
 *      JpaRepository<实体类，主键>
 *      JpaSpecificationExecutor<实体类>
 */
public interface CompanyDao extends JpaRepository<Company , String> , JpaSpecificationExecutor<Company> {

    Company findByName(String companyName);
}
