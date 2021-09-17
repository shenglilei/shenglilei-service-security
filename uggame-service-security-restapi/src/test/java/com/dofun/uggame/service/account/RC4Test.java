package com.dofun.uggame.service.account;

import com.dofun.uggame.common.util.RC4Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RC4Test {
    public static void main(String[] args) {
        String encryptData = "7D2AEAB5B02986077414";
        String key = "df831c18fa703096";
        String plainData = "VTO6UFLR7N3O7M2T";
        log.info("{},{}", RC4Util.decry("5C08A0F1B42B870571", key), plainData);
        log.info("{},{}", encryptData, RC4Util.encrypt(plainData, key));
    }
}
