package com.dofun.uggame.service.security;


import com.dofun.uggame.framework.core.annotation.EnableUGGameApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 安全服务
 */
@Slf4j
@EnableUGGameApplication
public class ServiceSecurityApplication {
    public static void main(String[] args) {
        log.info("系统目前默认时区为：{},现在时间是：{}", TimeZone.getDefault().getID(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //设置为越南的时区
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        log.info("修改时区为：{},", timeZone.getID());
        TimeZone.setDefault(timeZone);
        log.info("系统目前默认时区为：{},现在时间是：{}", TimeZone.getDefault().getID(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        SpringApplication.run(ServiceSecurityApplication.class, args);
    }
}
