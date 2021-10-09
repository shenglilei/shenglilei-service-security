package com.dofun.uggame.service.security.service.security;

import com.dofun.uggame.service.security.clientapi.pojo.request.WechatRobotMarkdownRequestParam;

/**
 * Achin
 *
 * @author Administrator
 * @since 2021-10-09
 */
public interface WechatService {

    void sendWechatRobotMarkdownMsg(WechatRobotMarkdownRequestParam param);

}
