package com.dofun.uggame.service.account;

import com.alibaba.fastjson.JSON;
import com.dofun.uggame.common.util.HttpSQSUtil;
import com.dofun.uggame.common.util.RC4Util;
import com.dofun.uggame.common.util.RandomUtil;
import com.dofun.uggame.framework.core.id.IdUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class HTTPSQSTest {
    @SneakyThrows
    public static void main(String[] args) {
        String ip = "161.117.249.164";
        Integer port = 1218;
        String topicName = "test20200916";
        String auth = null;
        List<Message> messageList=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            messageList.add(Message.builder()
                            .id((long) RandomUtil.getInt(100000))
                            .sex(RandomUtil.getInt(10))
                            .name(RandomUtil.getMixed(16))
                            .createTime(new Date())
                    .build());
            Thread.sleep(1000);
            log.info("message:{}",JSON.toJSON(messageList.get(i)));
        }
        HttpSQSUtil.put(ip,port,auth,topicName,messageList);
        List<Message> list=HttpSQSUtil.get(ip,port,auth,topicName,Message.class);
        for (Message message:list){
            log.info("message:{}",JSON.toJSON(message));
        }
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message{
        private Long id;
        private String name;
        private Integer sex;
        private Date createTime;
    }
}
