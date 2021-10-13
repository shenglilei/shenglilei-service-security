package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "公用对象-校验cookie请求参数")
@Data
public class CommonCheckCookieRequestParam extends BaseRequestParam {

    @ApiModelProperty(value = "货架ID")
    @NotNull(message = "货架ID:不能为空")
    private Integer hid;

    @ApiModelProperty(value = "当前用户Id")
    private Integer accessUserId;

}
