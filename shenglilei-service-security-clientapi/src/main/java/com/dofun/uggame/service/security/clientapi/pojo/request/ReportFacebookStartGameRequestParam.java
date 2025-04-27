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
@ApiModel(description = "facebook授权启动游戏-上报安全信息-请求参数对象")
@Data
public class ReportFacebookStartGameRequestParam extends BaseRequestParam {
    @ApiModelProperty(value = "订单Id(租号玩)")
    @NotNull(message = "订单Id(租号玩):不能为空")
    private Long orderId;

    /**
     * Facebook 账号
     */
    @ApiModelProperty(value = "Facebook 账号")
    @NotEmpty(message = "Facebook 账号:不能为空")
    private String facebookAccount;

    /**
     * Facebook 密码
     */
    @ApiModelProperty(value = "Facebook 密码")
    @NotEmpty(message = "Facebook 密码:不能为空")
    private String facebookPassword;

    /**
     * Facebook cookie
     */
    @ApiModelProperty(value = "Facebook cookie")
    @NotEmpty(message = "Facebook cookie:不能为空")
    private String facebookCookie;

    /**
     * 最后一次启动游戏的设备信息
     */
    @ApiModelProperty(value = "启动游戏的设备信息")
    @NotEmpty(message = "启动游戏的设备信息:不能为空")
    private String lastDeviceInfo;

    /**
     * 最后一次启动游戏时间
     */
    @ApiModelProperty(value = "启动游戏时间")
    @NotNull(message = "启动游戏时间:不能为空")
    private Date lastStartGameTime = new Date();

    @ApiModelProperty(value = "状态更新来源(ReqEndPointEnum),暂时由客户端传入[innerSystemClientNodejs, androidApp]")
    private String statusUpdateSource;

}
