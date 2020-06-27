package com.ihrm.domain.system.response;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName
 * @Description
 * @Author He.Wang
 * @Date 2020/6/9 20:07
 * @Version 1.0
 **/

@Getter
@Setter
public class RoleResult implements Serializable {

    @Id
    private String id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 说明
     */
    private String description;
    /**
     * 企业id
     */
    private String companyId;

    private List<String> permIds = new ArrayList<>();

    public RoleResult(Role role){
        BeanUtils.copyProperties(role,this);
        for (Permission permission : role.getPermissions()) {
            this.permIds.add(permission.getId());
        }
    }

}