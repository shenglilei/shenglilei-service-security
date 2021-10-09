package com.dofun.uggame.service.security.clientapi.pojo.request;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * WechatRobotMarkdownRequestParam
 *
 * @author Achin
 * @since 2021-10-09
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@Data
public class WechatRobotMarkdownRequestParam extends BaseRequestParam {

    @ApiModelProperty(value = "消息内容，markdown内容，最长不超过4096个字节，必须是utf8编码", required = true)
    @NotNull(message = "消息内容:不能为空")
    private String content;

}
