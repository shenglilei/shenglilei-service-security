package com.dofun.uggame.service.security.clientapi.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/15
 * Time:17:24
 * 订单状态
 */
public enum OrderStatusEnum {
    NORMAL(0, "正常"),
    CANCEL_USER(10, "用户撤单"),
    ;

    @Getter
    private Integer code;
    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
