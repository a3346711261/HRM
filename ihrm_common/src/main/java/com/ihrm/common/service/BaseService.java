package com.ihrm.common.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @ClassName
 * @Description
 * @Author He.Wang
 * @Date 2020/6/6 23:59
 * @Version 1.0
 **/

public class BaseService<T> {

    protected Specification<T> getSpec(String companyId){
        Specification<T> spec = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cb.equal(root.get("companyId").as(String.class),companyId);
            }
        };
        return spec;
    }

}