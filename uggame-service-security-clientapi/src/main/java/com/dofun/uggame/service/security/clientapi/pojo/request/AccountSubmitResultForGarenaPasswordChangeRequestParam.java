package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import com.dofun.uggame.service.security.clientapi.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "账号服务-提交garena密码的修改结果-请求参数对象")
@Data
public class AccountSubmitResultForGarenaPasswordChangeRequestParam extends BaseRequestParam {
    @ApiModelProperty(value = "货架Id（号主的某个游戏账号Id）")
    @NotNull(message = "货架Id:不能为空")
    @Range(message = "货架Id需要大于零", min = 1)
    private Integer hId;

    @ApiModelProperty(value = "状态：1=成功处理；2=处理失败")
    @NotNull(message = "状态:不能为空")
    @Range(message = "状态：1=成功处理；2=处理失败", min = 0, max = 2)
    private Integer status = StatusEnum.SUCCESS.getCode();

    @ApiModelProperty(value = "Garena 新密码")
    private String garenaPassword;
}
