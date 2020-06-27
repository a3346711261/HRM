package com.ihrm.domain.employee;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


//员工离职申请实体类
@Entity
@Table(name = "em_resignation")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE)
public class EmployeeResignation implements Serializable {
    private static final long serialVersionUID = 2890789302883962744L;
    /**
     * 员工Id
     */
    @Id
    private String userId;
    /**
     * 离职时间
     */
    private String resignationTime;
    /**
     * 离职类型
     */
    private String typeOfTurnover;
    /**
     * 申请离职原因
     */
    private String reasonsForLeaving;
    /**
     * 补偿金
     */
    private String compensation;
    /**
     * 代通知金
     */
    private String notifications;
    /**
     * 社保减员月
     */
    private String socialSecurityReductionMonth;
    /**
     * 公积金减员月
     */
    private String providentFundReductionMonth;
    /**
     * 图片
     */
    private String picture;
    /**
     * 创建时间
     */
    private Date createTime;
}