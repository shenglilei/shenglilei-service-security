package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BasePageRequestParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "账号服务-查询需要修改garena密码的账号列表-请求参数对象")
@Data
public class AccountQueryGarenaChangePasswordListRequestParam extends BasePageRequestParam {
}
