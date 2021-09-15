package com.dofun.uggame.service.security.clientapi.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/15
 * Time:17:24
 * 状态：0=待处理；1=成功处理；2=处理失败
 */
public enum StatusEnum {
    WAIT(0, "待处理"),
    SUCCESS(10, "成功处理"),
    FAILED(20, "处理失败"),
    ;

    @Getter
    private Integer code;
    private String message;

    StatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
