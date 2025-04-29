package com.dofun.shenglilei.service.security.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dofun.shenglilei.framework.mysql.entity.BaseEntity;
import lombok.*;

/**
 * 
 *
 * @author Sheng
 * @since 2025-04-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tb_qq_empower")
public class QqEmpowerEntity extends BaseEntity {
    /**
     * 
     */
    @TableId
    private Long id;
    /**
     * Q号
     */
    private String account;
    /**
     * 账号
     */
    private String zh;
    /**
     * 保存路径
     */
    private String fileUrl;
    /**
     * 货架号
     */
    private String hid;
    /**
     * 下载数据的url
     */
    private String dataUrl;
    /**
     * 设备sign信息
     */
    private String deviceSign;
    /**
     * 设备信息json
     */
    private String devicesJsonStr;
    /**
     * 修改数据时候的key
     */
    private String deviceKey;
    /**
     * device数据
     */
    private String keyCtxJsonStr;
    /**
     * 手表key
     */
    private String watchQqKeyJsonStr;
    /**
     * 配置信息
     */
    private String config;
    /**
     * 地区 省-市  如：河南-郑州
     */
    private String area;
    /**
     * 开通类型 cloud vbox
     */
    private String openType;
    /**
     * 开通时间
     */
    private Date openTime;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Date updateTime;
}
