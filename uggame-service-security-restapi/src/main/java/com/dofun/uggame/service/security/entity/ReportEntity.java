package com.dofun.uggame.service.security.entity;

import com.dofun.uggame.framework.mysql.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`zhw_security_report_facebook`")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class ReportEntity extends BaseEntity {
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
     * Facebook 账号
     */
    private String facebookAccount;

    /**
     * Facebook 密码
     */
    private String facebookPassword;

    /**
     * Facebook cookie
     */
    private String facebookCookie;

    /**
     * 最后一次启动游戏的设备信息
     */
    private String lastDeviceInfo;

    /**
     * 最后一次启动游戏时间
     */
    private Date lastStartGameTime;

    /**
     * 状态：0=待处理；1=成功处理；2=处理失败
     */
    private Integer status;

    /**
     * 状态更新来源(ReqEndPointEnum)
     */
    private String statusUpdateSource;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}