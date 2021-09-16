package com.dofun.uggame.service.account;

import com.alibaba.fastjson.JSON;
import com.dofun.uggame.common.util.HttpSQSUtil;
import com.dofun.uggame.common.util.RandomUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class HTTPSQSTest {
    @SneakyThrows
    public static void main(String[] args) {
        String ip = "161.117.249.164";
        Integer port = 1218;
        String topicName = "test20200916";
        String auth = null;
        Message message = Message.builder()
                .id((long) RandomUtil.getInt(100000))
                .sex(RandomUtil.getInt(10))
                .name(RandomUtil.getMixed(16))
                .createTime(new Date())
                .build();
        Thread.sleep(1000);
        log.info("message before:{}", JSON.toJSONString(message));
        HttpSQSUtil.put(ip, port, auth, topicName, JSON.toJSONString(message));
        Message message1 = HttpSQSUtil.get(ip, port, auth, topicName, Message.class);
        log.info("message after:{}", JSON.toJSONString(message1));
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
