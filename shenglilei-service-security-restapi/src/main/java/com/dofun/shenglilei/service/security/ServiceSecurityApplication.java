package com.dofun.shenglilei.service.security;


import com.dofun.shenglilei.framework.common.utils.TimezoneUtil;
import com.dofun.shenglilei.framework.core.annotation.EnableShengLiLeiApplication;
import org.springframework.boot.SpringApplication;

import static com.dofun.shenglilei.framework.common.constants.Constants.SYSTEM_DEFAULT_CONFIG_TIMEZONE;

/**
 * 安全服务
 */
@EnableShengLiLeiApplication
public class ServiceSecurityApplication {
    public static void main(String[] args) {
        //设置时区
        TimezoneUtil.set(SYSTEM_DEFAULT_CONFIG_TIMEZONE);
        SpringApplication.run(ServiceSecurityApplication.class, args);
    }
}
