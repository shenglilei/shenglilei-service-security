package com.dofun.uggame.service.security.task;

import com.dofun.uggame.service.security.service.security.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class GarenaCheckTask {

    @Autowired
    private AccountService accountService;

    /**
     * 每20秒检测Garena是否在正常工作
     */
    @Scheduled(fixedDelay = 60000)
    public void checkGarena() {
        accountService.checkGarena();
    }
}
