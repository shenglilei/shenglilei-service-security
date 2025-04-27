package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BasePageRequestParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "最近通过facebook授权启动游戏的记录-查询安全信息-请求参数对象")
@Data
public class ReportRecentFacebookStartGameRequestParam extends BasePageRequestParam {
    /**
     * 时间区间
     */
    @ApiModelProperty(value = "时间区间，单位：秒", hidden = true)
    private Long timeInterval = 60L;

    @ApiModelProperty(value = "状态更新来源(ReqEndPointEnum),暂时由客户端传入[innerSystemClientNodejs, androidApp]")
    private String statusUpdateSource;

}
