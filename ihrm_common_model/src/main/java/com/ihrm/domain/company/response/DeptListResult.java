package com.ihrm.domain.company.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName
 * @Description
 * @Author He.Wang
 * @Date 2020/6/6 23:41
 * @Version 1.0
 **/

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class DeptListResult {

    private String companyId;
    private String companyName;
    private String companyManage;  //公司联系人
    private List<Department> depts;

    public DeptListResult(Company company, List depts) {
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.companyManage = company.getLegalRepresentative();//公司联系人
        this.depts = depts;
    }
}
