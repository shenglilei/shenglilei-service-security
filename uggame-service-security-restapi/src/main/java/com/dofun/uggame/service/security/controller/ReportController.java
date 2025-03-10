package com.dofun.uggame.service.security.controller;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.service.security.clientapi.interfaces.ReportInterface;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportQuitFacebookAccountRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportRecentFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportWechatRobotRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportFacebookStartGameResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportRecentFacebookStartGameResponseParam;
import com.dofun.uggame.service.security.elasticsearch.ElasticsearchServiceUtil;
import com.dofun.uggame.service.security.service.security.ReportService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "上报安全信息")
@RestController
@Slf4j
public class ReportController implements ReportInterface {

    @Resource
    private ReportService reportService;
    @Resource
    private ElasticsearchServiceUtil elasticsearchServiceUtil;

    @Override
    public WebApiResponse<ReportFacebookStartGameResponseParam> startFacebookGame(@RequestBody @Validated ReportFacebookStartGameRequestParam param) {
        //强制设置为当前时间
        param.setLastStartGameTime(new Date());
        ReportFacebookStartGameResponseParam responseParam = reportService.startFacebookGame(param);
        return WebApiResponse.success(responseParam);
    }

    @Override
    public WebApiResponse saveFbLoginEvents(@RequestBody Map<String, Object> map) throws IOException {
        if (map == null) {
            log.info("fb上报参数为空");
            map = new HashMap<>();
            map.put("reason", "0");//上送参数为空
        }
        if (map.get("eventType") == null) {
            log.info("上送事件类型参数为空");
            map.put("reason", "1");//上送事件类型参数为空
        }
        map.put("date", new Date());
        elasticsearchServiceUtil.documentInsert(map, "fb_report");
        return WebApiResponse.success();
    }

    @Override
    public WebApiResponse<ReportRecentFacebookStartGameResponseParam> queryRecentStartGameLog(@Validated ReportRecentFacebookStartGameRequestParam param) {
        ReportRecentFacebookStartGameResponseParam responseParam = reportService.queryRecentStartGameLog(param);
        return WebApiResponse.success(responseParam);
    }

    @Override
    public WebApiResponse<BaseResponseParam> quitFacebookAccount(@RequestBody @Validated ReportQuitFacebookAccountRequestParam param) {
        if (!(param.getStatus() == 10 || param.getStatus() == 20)) {
            throw new IllegalArgumentException("状态不正确:[10、20]");
        }
        reportService.quitFacebookAccount(param);
        return WebApiResponse.success(BaseResponseParam.empty());
    }

    @Override
    public WebApiResponse<BaseResponseParam> sendWechatRobot(ReportWechatRobotRequestParam param) {
        // 根据orderId查询数据
        reportService.sendWechatRobot(param);
        return WebApiResponse.success(BaseResponseParam.empty());
    }
}
