package com.dofun.uggame.service.account;

import com.alibaba.fastjson.JSON;
import com.dofun.uggame.common.util.HttpSQSUtil;
import com.dofun.uggame.common.util.RandomUtil;
import com.dofun.uggame.service.security.clientapi.enums.OrderStatusEnum;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountReceiveGarenaChangePasswordRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountSubmitResultForGarenaPasswordChangeRequestParam;
import com.dofun.uggame.service.security.constants.HttPSQSConstants;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

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
        send2java();
    }

    private static void send2php() {
        String topicName = HttPSQSConstants.QUEUE_DEFINE_NOTIFY_PHP_GARENA_PASSWORD_CHANGE_SUCCESS;
        AccountSubmitResultForGarenaPasswordChangeRequestParam param = new AccountSubmitResultForGarenaPasswordChangeRequestParam();
        param.setStatus(1);
        param.setGarenaPassword("654321");
        param.setHaoId(1212121212);
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
        param.setOrderEndTime(new Date(System.currentTimeMillis() + (long) RandomUtil.getInt(10000000)));
        param.setOrderStartTime(new Date(System.currentTimeMillis() - (long) RandomUtil.getInt(100000)));
        param.setOrderStatus(RandomUtils.nextBoolean() ? OrderStatusEnum.NORMAL.getCode() : OrderStatusEnum.CANCEL_USER.getCode());
        log.info("message before:{}", JSON.toJSONString(param));
        HttpSQSUtil.put(ip, port, auth, topicName, JSON.toJSONString(param));
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
