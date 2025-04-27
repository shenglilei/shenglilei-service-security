package com.dofun.uggame.service.security.clientapi.pojo.response;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "账号服务-登录-响应参数对象")
@Data
@Builder
public class AccountLoginForGarenaChangePasswordResponseParam extends BaseResponseParam {

    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    @ApiModelProperty(value = "令牌失效时间")
    private Date expireAt;
}