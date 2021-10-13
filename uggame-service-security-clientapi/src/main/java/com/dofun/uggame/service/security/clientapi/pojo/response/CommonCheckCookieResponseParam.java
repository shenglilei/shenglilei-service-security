package com.dofun.uggame.service.security.clientapi.pojo.response;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "facebook授权启动游戏-上报安全信息-响应参数对象")
@Data
public class CommonCheckCookieResponseParam extends BaseResponseParam {
    @ApiModelProperty(value = "状态[0-无效的 1-有效的]")
    private Integer status;
}