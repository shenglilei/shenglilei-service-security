package com.dofun.uggame.service.security.entity;

import com.dofun.uggame.framework.mysql.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`zhw_security_account_garena`")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class AccountEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 订单Id(租号玩)
     */
    private Long orderId;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单开始时间
     */
    private Date orderStartTime;

    /**
     * 订单结束时间
     */
    private Date orderEndTime;

    /**
     * 货架Id
     */
    private Integer haoId;

    /**
     * Garena 账号
     */
    private String garenaAccount;

    /**
     * Garena 密码
     */
    private String garenaPassword;

    /**
     * Garena 令牌
     */
    private String garenaKey;

    /**
     * 状态：0=待处理；1=成功处理；2=处理失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}