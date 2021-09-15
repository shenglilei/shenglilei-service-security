package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import com.dofun.uggame.service.security.clientapi.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "facebook授权设备列表退出结果-上报安全信息-请求参数对象")
@Data
@ToString
public class ReportQuitFacebookAccountRequestParam extends BaseRequestParam {
    @ApiModelProperty(value = "上报记录Id")
    @NotNull(message = "上报记录Id:不能为空")
    private Long id;

    @ApiModelProperty(value = "状态：0=待处理；1=成功处理；2=处理失败")
    @NotNull(message = "状态:不能为空")
    @Range(message = "状态：0=待处理；1=成功处理；2=处理失败", min = 0, max = 2)
    private Integer status = StatusEnum.SUCCESS.getCode();
}
