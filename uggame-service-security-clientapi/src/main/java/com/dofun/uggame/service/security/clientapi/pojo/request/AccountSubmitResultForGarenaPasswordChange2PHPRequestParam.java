package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "账号服务-提交garena密码的修改结果-请求参数对象")
@Data
public class AccountSubmitResultForGarenaPasswordChange2PHPRequestParam extends BaseRequestParam {

    @ApiModelProperty(value = "货架Id")
    @NotNull(message = "货架Id:不能为空")
    @Range(message = "货架Id需要大于零", min = 1)
    private Integer haoId;

    @ApiModelProperty(value = "Garena 新密码")
    private String garenaPassword;
}
