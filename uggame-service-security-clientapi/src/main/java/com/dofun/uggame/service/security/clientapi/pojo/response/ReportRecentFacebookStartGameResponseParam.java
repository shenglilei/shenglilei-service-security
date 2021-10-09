package com.dofun.uggame.service.security.clientapi.pojo.response;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "最近通过facebook授权启动游戏的记录-查询安全信息-响应参数对象")
@Data
@Builder
public class ReportRecentFacebookStartGameResponseParam extends BaseResponseParam {
    @ApiModelProperty(value = "详细记录信息")
    List<ReportRecentFacebookStartGameItemResponseParam> item;

    @ApiModel(description = "最近通过facebook授权启动游戏的记录-查询安全信息-响应参数对象")
    @Data
    @Builder
    public static class ReportRecentFacebookStartGameItemResponseParam {
        @ApiModelProperty(value = "上报记录Id")
        private Long id;

        @ApiModelProperty(value = "订单Id(租号玩)")
        private Long orderId;

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
         * Facebook cookie
         */
        @ApiModelProperty(value = "Facebook cookie")
        private String facebookCookie;

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

        /**
         * 状态：0=待处理；1=成功处理；2=处理失败
         */
        @ApiModelProperty(value = "状态：0=待处理；1=成功处理；2=处理失败")
        private Integer status;

        @ApiModelProperty(value = "状态更新来源(ReqEndPointEnum)[innerSystemClientNodejs, androidApp]")
        private String statusUpdateSource;
    }
}