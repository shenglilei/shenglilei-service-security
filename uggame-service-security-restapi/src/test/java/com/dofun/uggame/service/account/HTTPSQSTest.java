package com.dofun.uggame.service.account;

import com.alibaba.fastjson.JSON;
import com.dofun.uggame.common.util.HttpSQSUtil;
import com.dofun.uggame.common.util.RandomUtil;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountSubmitResultForGarenaPasswordChangeRequestParam;
import com.dofun.uggame.service.security.constants.HttPSQSConstants;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class HTTPSQSTest {
    @SneakyThrows
    public static void main(String[] args) {
        String ip = "161.117.249.164";
        Integer port = 1218;
        String topicName = HttPSQSConstants.QUEUE_DEFINE_NOTIFY_PHP_GARENA_PASSWORD_CHANGE_SUCCESS;
        String auth = null;
        AccountSubmitResultForGarenaPasswordChangeRequestParam param = new AccountSubmitResultForGarenaPasswordChangeRequestParam();
        param.setStatus(1);
        param.setGarenaPassword("654321");
        param.setHId(1212121212);
        log.info("message before:{}", JSON.toJSONString(param));
        HttpSQSUtil.put(ip, port, auth, topicName, JSON.toJSONString(param));
//        Message message1 = HttpSQSUtil.get(ip, port, auth, topicName, Message.class);
//        log.info("message after:{}", JSON.toJSONString(message1));
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
