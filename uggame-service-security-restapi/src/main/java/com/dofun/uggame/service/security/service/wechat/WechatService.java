package com.dofun.uggame.service.security.service.wechat;

import com.dofun.uggame.service.security.clientapi.pojo.request.WechatRobotRequestParam;

/**
 * Achin
 *
 * @author Administrator
 * @since 2021-10-09
 */
public interface WechatService {

    void sendWechatRobotMarkdownMsg(WechatRobotRequestParam param);

    void sendWechatRobotTextMsg(WechatRobotRequestParam param);

}
