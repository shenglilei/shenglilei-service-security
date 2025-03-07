package com.dofun.uggame.service.security.mq.httpsqs;

import com.alibaba.fastjson.JSON;
import com.dofun.uggame.common.util.HttpSQSUtil;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountReceiveGarenaChangePasswordRequestParam;
import com.dofun.uggame.service.security.constants.HttPSQSConstants;
import com.dofun.uggame.service.security.service.security.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/16
 * Time:17:33
 */
@Slf4j
@Component
@EnableScheduling
public class QueueMessageReceiver {
    @Autowired
    private AccountService accountService;

    @Value("${httpsqs.ip}")
    private String ip;

    @Value("${httpsqs.port}")
    private Integer port;

    @Value("${httpsqs.auth}")
    private String auth;

    @Scheduled(fixedDelay = 500)
    public void orderNeedChangeGarenaPassword() {
        AccountReceiveGarenaChangePasswordRequestParam param = HttpSQSUtil.get(ip, port, auth, HttPSQSConstants.QUEUE_DEFINE_NOTIFY_JAVA_ORDER_NEED_CHANGE_GARENA_PASSWORD, AccountReceiveGarenaChangePasswordRequestParam.class);
        if (param != null) {
            log.info("开始处理HTTPSQS消息:{}", JSON.toJSONString(param));
            accountService.receiveGarenaChangePassword(param);
        }
    }
}
