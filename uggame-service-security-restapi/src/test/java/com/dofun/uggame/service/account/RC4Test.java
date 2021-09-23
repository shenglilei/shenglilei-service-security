package com.dofun.uggame.service.account;

import com.dofun.uggame.common.util.RC4Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RC4Test {
    public static void main(String[] args) {
        String encryptData = "6B26A9F2B5238606";
        String key = "df831c18fa703096";
        String plainData = "pz904514";
        log.info("{},{}", RC4Util.decry(encryptData, key), plainData);
        log.info("{},{}", encryptData, RC4Util.encrypt(plainData, key));
    }
}
