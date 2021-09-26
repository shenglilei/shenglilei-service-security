package com.dofun.uggame.service.security;


import com.dofun.uggame.framework.core.annotation.EnableUGGameApplication;
import com.dofun.uggame.framework.core.utils.TimezoneUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

import static com.dofun.uggame.framework.core.constants.Constants.SYSTEM_DEFAULT_CONFIG_TIMEZONE;

/**
 * 安全服务
 */
@Slf4j
@EnableUGGameApplication
public class ServiceSecurityApplication {
    public static void main(String[] args) {
        //设置时区
        TimezoneUtil.set(SYSTEM_DEFAULT_CONFIG_TIMEZONE);
        SpringApplication.run(ServiceSecurityApplication.class, args);
    }
}
