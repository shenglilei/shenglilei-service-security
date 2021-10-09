package com.dofun.uggame.service.security.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.dofun.uggame.framework.mysql.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 订单列表
 *
 * @author Achin
 * @since 2021-10-09
 */
@Table(name = "zhw_dingdan")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class DingdanEntity extends BaseEntity {
    /**
     * ID
     */
    @Id
    private Long id;
    /**
     * 租客ID
     */
    private Integer uid;
    /**
     * 帐号编号
     */
    private Integer hid;
    /**
     * 号主ID
     */
    private Integer hUid;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 租号玩订单id
     */
    private Integer zhwOrderId;
    /**
     * 订单描述
     */
    private String title;
    /**
     * 租赁时长
     */
    private Integer hours;
    /**
     * 每小时租金
     */
    private BigDecimal price;
    /**
     * 保证金【不参与订单计算】
     */
    private BigDecimal bzmoney;
    /**
     * 实付金额【订单金额，不含保证金】
     */
    private BigDecimal realmoney;
    /**
     * 订单总金额【订单金额，不含保证金】
     */
    private BigDecimal amount;
    /**
     * 租客余额
     */
    private BigDecimal balance;
    /**
     * 开始时间
     */
    private Date stimer;
    /**
     * 结束时间
     */
    private Date etimer;
    /**
     * 游戏ID
     */
    private Integer gameId;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 大区ID
     */
    private Integer zoneId;
    /**
     * 大区名称
     */
    private String zoneName;
    /**
     * 服务器ID
     */
    private Integer serverId;
    /**
     * 服务器名称
     */
    private String serverName;
    /**
     * 备注：号主要求
     */
    private String memo;
    /**
     * 段位
     */
    private String attrDw;
    /**
     * 角色名
     */
    private String attrJsm;
    /**
     * 英雄数量
     */
    private Integer attrHero;
    /**
     * 皮肤数量
     */
    private Integer attrSkin;
    /**
     * 盒子的战绩标记
     */
    private String attrBox;
    /**
     * 1是允许打排位 2不允许打排位
     */
    private Integer attrBay;
    /**
     * 解锁码
     */
    private String attrLock;
    /**
     * 租赁方式：1-租赁，2-预约
     */
    private Integer rentWay;
    /**
     * 租赁类型：1-时租，8-包夜，10-10小时，24-日租,168-周租
     */
    private Integer rentType;
    /**
     * 订单日志（租赁、续租）
     */
    private String rentRecord;
    /**
     * 订单状态：0-为正常，1-投诉中，2-正常结算，3-撤单，4-取消预约
     */
    private Integer zt;
    /**
     * 上号状态：0-未上号，1-已上号
     */
    private Integer ztTelent;
    /**
     * 续租状态：0-未续租，1-已续租
     */
    private Integer ztRelet;
    /**
     * 保证金退回状态：0-未退回，1-已退回，2-已扣除
     */
    private Integer ztBzmoney;
    /**
     * 评价状态：0-等待评价, 1-买方已评价, 2-卖家回评
     */
    private Integer ztReview;
    /**
     * 自动投诉：0-否，1-是
     */
    private Integer ztAutoTs;
    /**
     * 用户ip
     */
    private String ip;
    /**
     * 下单来源：1001-PC，2001-M，3001-客户端，4001-APP
     */
    private Integer addFrom;
    /**
     * 订单生成时间
     */
    private Date addtime;
    /**
     * 更新时间
     */
    private Date uptime;
    /**
     * 开始状态：0-未开始，1-已开始
     */
    private Integer ztRun;
}
