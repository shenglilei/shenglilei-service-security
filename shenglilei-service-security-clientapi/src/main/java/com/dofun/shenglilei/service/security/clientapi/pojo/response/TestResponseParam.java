package com.dofun.shenglilei.service.security.clientapi.pojo.response;


import com.dofun.shenglilei.framework.common.base.BaseResponseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestResponseParam extends BaseResponseParam {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "核对日期")
    private String checkDate;

    @ApiModelProperty(value = "渠道编码")
    private Integer payViaCode;

    @ApiModelProperty(value = "渠道编码名称")
    private String payViaName;

    @ApiModelProperty(value = "第三方充值订单数")
    private Integer thirdPartyRechargeNum;

    @ApiModelProperty(value = "第三方充值金额")
    private BigDecimal thirdPartyRechargeMoney;

    @ApiModelProperty(value = "本系统充值数")
    private Integer systemRechargeNum;

    @ApiModelProperty(value = "本系统充值金额")
    private BigDecimal systemRechargeMoney;

    @ApiModelProperty(value = "订单差异数(第三方减去本系统)")
    private Integer orderDifferences;

    @ApiModelProperty(value = "资金差异数(第三方减去本系统)")
    private BigDecimal moneyDifferences;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "差异订单号")
    private String diffTradeNo;
}
