package com.dofun.uggame.service.security.clientapi.pojo.response;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "facebook授权启动游戏-上报安全信息-响应参数对象")
@Data
@Builder
public class ReportFacebookStartGameResponseParam extends BaseResponseParam {
    @ApiModelProperty(value = "上报记录Id")
    private Long id;
}