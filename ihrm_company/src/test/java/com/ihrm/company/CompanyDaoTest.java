package com.ihrm.company;

import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName conpanyDao测试类
 * @Description
 * @Author He.Wang
 * @Date 2020/6/5 11:30
 * @Version 1.0
 **/

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyDaoTest {

    @Autowired
    private CompanyDao companyDao;

    @Test
    public void test(){

//        companyDao.save(company);  保存或更新(id)
//        companyDao.deleteById("1");   根据id删除
//        companyDao.findById("1").get();   根据id查询
//        companyDao.findAll(); 全部数据查询

        Company company = companyDao.findById("1").get();
        System.out.println(company);
    }

    @Test
    public void test01(){

    }

}