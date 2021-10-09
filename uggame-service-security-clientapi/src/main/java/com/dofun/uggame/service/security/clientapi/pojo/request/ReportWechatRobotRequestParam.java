package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "facebook授权设备列表退出结果-上报安全信息-请求参数对象")
@Data
public class ReportWechatRobotRequestParam extends BaseRequestParam {

    @ApiModelProperty(value = "订单Id(租号玩)")
    @NotNull(message = "订单Id(租号玩):不能为空")
    private Long orderId;

    @ApiModelProperty(value = "消息类型")
    private Integer msgType;

    @ApiModelProperty(value = "额外说明内容")
    private String content;
}
