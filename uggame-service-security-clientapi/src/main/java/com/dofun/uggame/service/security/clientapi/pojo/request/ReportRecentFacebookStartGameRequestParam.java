package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "最近通过facebook授权启动游戏的记录-查询安全信息-请求参数对象")
@Data
@ToString
public class ReportRecentFacebookStartGameRequestParam extends BaseRequestParam {
    /**
     * 时间区间
     */
    @ApiModelProperty(value = "时间区间，单位：秒")
    private Long timeInterval = 60L;

}
