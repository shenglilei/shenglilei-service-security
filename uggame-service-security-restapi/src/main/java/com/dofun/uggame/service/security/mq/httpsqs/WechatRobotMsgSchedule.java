package com.dofun.uggame.service.security.mq.httpsqs;

import com.dofun.uggame.service.security.service.security.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class WechatRobotMsgSchedule {

    @Autowired
    private ReportService reportService;

    @Scheduled(cron = "0 0 23 * * ?")
    public void sendWechatRobotTextMsg() {
        reportService.sendReportStatisticsToWechatRobotMsg();
    }
}
