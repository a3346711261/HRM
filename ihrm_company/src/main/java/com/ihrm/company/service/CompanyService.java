package com.ihrm.company.service;

import cn.yukonga.yrpc.core.annotation.RemoteService;
import com.ihrm.common.utlis.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName Company业务层
 * @Description
 * @Author He.Wang
 * @Date 2020/6/5 11:53
 * @Version 1.0
 **/

@Service
public class CompanyService{

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存企业
     *  1.配置idWorker(雪花算法)到工程
     *  2.在service中注入idWorker
     *  3.通过idWorker生成Id
     *  4.保存企业
     */
    public void add(Company company){
        //基本属性的设置
        String id = idWorker.nextId()+"";
        company.setId(id);
        //默认的状态
        // 0代表未审核，1代表审核通过
        company.setAuditState("1");
        //0.未激活，1.已激活
        company.setState(1);
        companyDao.save(company);
    }

    /**
     * 更新企业
     *  1.参数,Company
     *  2.根据id查询企业对象
     *  3.设置修改的属性值
     *  4.调用dao完成更新
     */
    public void update(Company company){
        Company temp = companyDao.findById(company.getId()).get();
        temp.setName(company.getName());
        temp.setCompanyPhone(company.getCompanyPhone());
        companyDao.save(temp);
    }


    /**
     * 删除企业
     */
    public void deleteById(String id){
        companyDao.deleteById(id);
    }


    /**
     * 根据id查询企业
     */
    public Company findById(String id){
        Company company = companyDao.findById(id).get();
        return company;
    }


    /**
     * 查询企业列表
     */
    public List<Company> findAll(){
        List<Company> all = companyDao.findAll();
        return all;
    }


    public Company findByName(String companyName){
        Company company = companyDao.findByName(companyName);
        return company;
    }

}