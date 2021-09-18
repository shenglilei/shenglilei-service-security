package com.dofun.uggame.service.security.controller;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.service.security.clientapi.interfaces.ReportInterface;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportQuitFacebookAccountRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportRecentFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportFacebookStartGameResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportRecentFacebookStartGameResponseParam;
import com.dofun.uggame.service.security.service.security.ReportService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@Api(tags = "上报安全信息")
@RestController
@Slf4j
public class ReportController implements ReportInterface {

    @Resource
    private ReportService reportService;

    @Override
    public WebApiResponse<ReportFacebookStartGameResponseParam> startFacebookGame(@RequestBody @Validated ReportFacebookStartGameRequestParam param) {
        //强制设置为当前时间
        param.setLastStartGameTime(new Date());
        ReportFacebookStartGameResponseParam responseParam = reportService.startFacebookGame(param);
        return WebApiResponse.success(responseParam);
    }

    @Override
    public WebApiResponse<ReportRecentFacebookStartGameResponseParam> queryRecentStartGameLog(@Validated ReportRecentFacebookStartGameRequestParam param) {
        ReportRecentFacebookStartGameResponseParam responseParam = reportService.queryRecentStartGameLog(param);
        return WebApiResponse.success(responseParam);
    }

    @Override
    public WebApiResponse<BaseResponseParam> quitFacebookAccount(@RequestBody @Validated ReportQuitFacebookAccountRequestParam param) {
        reportService.quitFacebookAccount(param);
        return WebApiResponse.success(BaseResponseParam.empty());
    }
}
