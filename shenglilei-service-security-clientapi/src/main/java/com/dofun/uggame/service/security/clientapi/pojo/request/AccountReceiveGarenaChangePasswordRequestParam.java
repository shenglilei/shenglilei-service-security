package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "账号服务-接收需要修改garena密码的账号列表-请求参数对象")
@Data
public class AccountReceiveGarenaChangePasswordRequestParam extends BaseRequestParam {
    @ApiModelProperty(value = "订单Id(租号玩)")
    @NotNull(message = "订单Id(租号玩):不能为空")
    private Long orderId;

    @ApiModelProperty(value = "订单状态")
    @NotNull(message = "订单状态:不能为空")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单开始时间")
    private Date orderStartTime;

    @ApiModelProperty(value = "订单结束时间")
    @NotNull(message = "订单结束时间:不能为空")
    private Date orderEndTime;

    @ApiModelProperty(value = "货架Id")
    @NotNull(message = "货架Id:不能为空")
    private Integer haoId;

    @ApiModelProperty(value = "Garena 账号")
    @NotEmpty(message = "Garena 账号:不能为空")
    private String garenaAccount;

    @ApiModelProperty(value = "Garena 密码")
    @NotEmpty(message = "Garena 密码:不能为空")
    private String garenaPassword;

    @ApiModelProperty(value = "Garena 令牌")
    @NotEmpty(message = "Garena 令牌:不能为空")
    private String garenaKey;
}
