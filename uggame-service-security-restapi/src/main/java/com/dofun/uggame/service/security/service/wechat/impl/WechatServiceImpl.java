package com.dofun.uggame.service.security.service.wechat.impl;

import com.alibaba.fastjson.JSONObject;
import com.dofun.uggame.framework.common.error.impl.CommonError;
import com.dofun.uggame.framework.common.exception.BusinessException;
import com.dofun.uggame.service.security.clientapi.pojo.request.WechatRobotMarkdownRequestParam;
import com.dofun.uggame.service.security.service.wechat.WechatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Achin
 *
 * @author Administrator
 * @since 2021-10-09
 */
@Service
public class WechatServiceImpl implements WechatService {

    @Value("${wechat.robot.url}")
    private String wechatRobotUrl;

    @Value("${wechat.robot.key.securityReport}")
    private String wechatRobotKeySecurityReport;

    @Override
    public void sendWechatRobotMarkdownMsg(WechatRobotMarkdownRequestParam param) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "markdown");
        jsonObject.put("markdown", param);
        ResponseEntity<Map> mapResponseEntity = restTemplate.postForEntity(wechatRobotUrl + wechatRobotKeySecurityReport, jsonObject.toJSONString(), Map.class);
        if (mapResponseEntity.getBody() == null || mapResponseEntity.getBody().get("errcode") == null) {
            throw new BusinessException(CommonError.UNKNOWN_ERROR.getCode(), "微信接口异常~");
        }
        if (!mapResponseEntity.getBody().get("errcode").equals(0)) {
            throw new BusinessException(CommonError.UNKNOWN_ERROR.getCode(), mapResponseEntity.getBody().get("errmsg").toString());
        }
    }

    @Override
    public void sendWechatRobotTextMsg(WechatRobotMarkdownRequestParam param) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        JSONObject jsonObject = new JSONObject();
        JSONObject textJson = new JSONObject();
        textJson.put("content", param.getContent());
        textJson.put("mentioned_list", new String[]{"@all"});

        jsonObject.put("msgtype", "text");
        jsonObject.put("text", textJson);
        ResponseEntity<Map> mapResponseEntity = restTemplate.postForEntity(wechatRobotUrl + wechatRobotKeySecurityReport, jsonObject.toJSONString(), Map.class);
        if (mapResponseEntity.getBody() == null || mapResponseEntity.getBody().get("errcode") == null) {
            throw new BusinessException(CommonError.UNKNOWN_ERROR.getCode(), "微信接口异常~");
        }
        if (!mapResponseEntity.getBody().get("errcode").equals(0)) {
            throw new BusinessException(CommonError.UNKNOWN_ERROR.getCode(), mapResponseEntity.getBody().get("errmsg").toString());
        }
    }

}
