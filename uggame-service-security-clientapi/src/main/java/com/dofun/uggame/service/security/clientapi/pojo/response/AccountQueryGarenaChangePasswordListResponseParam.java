package com.dofun.uggame.service.security.clientapi.pojo.response;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "账号服务-查询需要修改garena密码的账号列表-响应参数对象")
@Data
@Builder
public class AccountQueryGarenaChangePasswordListResponseParam extends BaseResponseParam {
    @ApiModelProperty(value = "详细记录信息")
    List<AccountQueryGarenaChangePasswordItemResponseParam> item;

    @ApiModel(description = "账号服务-查询需要修改garena密码的账号列表-响应参数对象")
    @Data
    @Builder
    public static class AccountQueryGarenaChangePasswordItemResponseParam {

        @ApiModelProperty(value = "订单Id(租号玩)")
        private Long orderId;

        @ApiModelProperty(value = "货架Id")
        private Integer haoId;

        @ApiModelProperty(value = "Garena 账号")
        private String garenaAccount;

        @ApiModelProperty(value = "Garena 密码")
        private String garenaPassword;

        @ApiModelProperty(value = "Garena 令牌")
        private String garenaKey;
    }
}