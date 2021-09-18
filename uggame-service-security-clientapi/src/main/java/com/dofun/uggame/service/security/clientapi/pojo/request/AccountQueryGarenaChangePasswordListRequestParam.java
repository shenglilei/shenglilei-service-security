package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BasePageRequestParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "账号服务-查询需要修改garena密码的账号列表-请求参数对象")
@Data
public class AccountQueryGarenaChangePasswordListRequestParam extends BasePageRequestParam {
    @ApiModelProperty(value = "访问令牌")
    @NotEmpty(message = "访问令牌:不能为空")
    private String accessToken;
}
