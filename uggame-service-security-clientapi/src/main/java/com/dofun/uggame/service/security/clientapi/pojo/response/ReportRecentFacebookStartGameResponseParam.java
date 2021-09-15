package com.dofun.uggame.service.security.clientapi.pojo.response;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "最近通过facebook授权启动游戏的记录-查询安全信息-响应参数对象")
@Data
@ToString
@Builder
public class ReportRecentFacebookStartGameResponseParam extends BaseResponseParam {
    @ApiModelProperty(value = "详细记录信息")
    List<ReportRecentFacebookStartGameItemResponseParam> item;

    @ApiModel(description = "最近通过facebook授权启动游戏的记录-查询安全信息-响应参数对象")
    @Data
    @ToString
    @Builder
    public static class ReportRecentFacebookStartGameItemResponseParam {
        @ApiModelProperty(value = "上报记录Id")
        private Long id;

        @ApiModelProperty(value = "订单编码(租号玩)")
        private String zhwOrderNo;

        /**
         * Facebook 账号
         */
        @ApiModelProperty(value = "Facebook 账号")
        private String facebookAccount;

        /**
         * Facebook 密码
         */
        @ApiModelProperty(value = "Facebook 密码")
        private String facebookPassword;

        /**
         * 最后一次启动游戏的设备信息
         */
        @ApiModelProperty(value = "启动游戏的设备信息")
        private String lastDeviceInfo;

        /**
         * 最后一次启动游戏时间
         */
        @ApiModelProperty(value = "启动游戏时间")
        private Date lastStartGameTime;
    }
}