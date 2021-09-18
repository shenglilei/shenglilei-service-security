package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "账号服务-登录-请求参数对象")
@Data
public class AccountLoginForGarenaChangePasswordRequestParam extends BaseRequestParam {
    @ApiModelProperty(value = "登录账号")
    @NotEmpty(message = "登录账号:不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码")
    @NotEmpty(message = "登录密码:不能为空")
    private String password;

    @ApiModelProperty(value = "终端类型", example = "garenaPasswordAgent")
    private String clientType;
}
