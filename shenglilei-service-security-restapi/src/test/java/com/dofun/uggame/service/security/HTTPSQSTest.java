package com.dofun.uggame.service.security;

import com.alibaba.fastjson.JSON;
import com.dofun.uggame.common.util.HttpSQSUtil;
import com.dofun.uggame.common.util.RandomUtil;
import com.dofun.uggame.framework.common.base.BaseRequestParam;
import com.dofun.uggame.service.security.clientapi.enums.OrderStatusEnum;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountSubmitResultForGarenaPasswordChange2PHPRequestParam;
import com.dofun.uggame.service.security.constants.HttPSQSConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Slf4j
public class HTTPSQSTest {
    static String ip = "161.117.249.164";
    static Integer port = 1218;
    static String auth = null;

    @SneakyThrows
    public static void main(String[] args) {
//        Message message1 = HttpSQSUtil.get(ip, port, auth, topicName, Message.class);
//        log.info("message after:{}", JSON.toJSONString(message1));
//        send2php();
//        send2java();

        AccountReceiveGarenaChangePasswordRequestParam param = HttpSQSUtil.get(ip, port, auth, HttPSQSConstants.QUEUE_DEFINE_NOTIFY_JAVA_ORDER_NEED_CHANGE_GARENA_PASSWORD, AccountReceiveGarenaChangePasswordRequestParam.class);
        if (param != null) {
            log.info("开始处理HTTPSQS消息:{}", JSON.toJSONString(param));
        }
    }

    private static void send2php() {
        String topicName = HttPSQSConstants.QUEUE_DEFINE_NOTIFY_PHP_GARENA_PASSWORD_CHANGE_SUCCESS;
        AccountSubmitResultForGarenaPasswordChange2PHPRequestParam param = new AccountSubmitResultForGarenaPasswordChange2PHPRequestParam();
        param.setGarenaPassword("7939ABF4B2298401");
        param.setHaoId(2616781);
        log.info("message before:{}", JSON.toJSONString(param));
        HttpSQSUtil.put(ip, port, auth, topicName, JSON.toJSONString(param));
    }

    private static void send2java() {
        String topicName = HttPSQSConstants.QUEUE_DEFINE_NOTIFY_JAVA_ORDER_NEED_CHANGE_GARENA_PASSWORD;
        AccountReceiveGarenaChangePasswordRequestParam param = new AccountReceiveGarenaChangePasswordRequestParam();
        param.setGarenaKey(RandomUtil.getMixed(16));
        param.setGarenaAccount(RandomUtil.getMixed(16));
        param.setGarenaPassword(RandomUtil.getMixed(16));
        param.setHaoId(RandomUtil.getInt(10000000));
        param.setOrderId((long) RandomUtil.getInt(10000000));
//        param.setOrderEndTime(System.currentTimeMillis() + (long) RandomUtil.getInt(10000000));
        param.setOrderEndTime(1632368831000L);
//        param.setOrderStartTime(System.currentTimeMillis() - (long) RandomUtil.getInt(100000));
        param.setOrderStartTime(1632364931000L);
//        param.setOrderStatus(RandomUtils.nextBoolean() ? OrderStatusEnum.NORMAL.getCode() : OrderStatusEnum.CANCEL_USER.getCode());
        param.setOrderStatus(OrderStatusEnum.NORMAL.getCode());
        log.info("message before:{}", JSON.toJSONString(param));
        HttpSQSUtil.put(ip, port, auth, topicName, JSON.toJSONString(param));
    }


    @EqualsAndHashCode(callSuper = true)
    @ApiModel(description = "账号服务-接收需要修改garena密码的账号列表-请求参数对象")
    @Data
    public static class AccountReceiveGarenaChangePasswordRequestParam extends BaseRequestParam {
        @ApiModelProperty(value = "订单Id(租号玩)")
        @NotNull(message = "订单Id(租号玩):不能为空")
        private Long orderId;

        @ApiModelProperty(value = "订单状态")
        @NotNull(message = "订单状态:不能为空")
        private Integer orderStatus;

        @ApiModelProperty(value = "订单开始时间")
        private Long orderStartTime;

        @ApiModelProperty(value = "订单结束时间")
        @NotNull(message = "订单结束时间:不能为空")
        private Long orderEndTime;

        @ApiModelProperty(value = "货架Id")
        @NotNull(message = "货架Id:不能为空")
        private Integer haoId;

        @ApiModelProperty(value = "Garena 账号")
        @NotEmpty(message = "Garena 账号:不能为空")
        private String garenaAccount;

        @ApiModelProperty(value = "Garena 密码")
        @NotEmpty(message = "Garena 密码:不能为空")
        private String garenaPassword;

        @ApiModelProperty(value = "Garena 令牌")
        @NotEmpty(message = "Garena 令牌:不能为空")
        private String garenaKey;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private Long id;
        private String name;
        private Integer sex;
        private Date createTime;
    }
}
