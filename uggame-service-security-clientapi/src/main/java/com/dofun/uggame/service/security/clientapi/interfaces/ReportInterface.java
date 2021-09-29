package com.dofun.uggame.service.security.clientapi.interfaces;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportQuitFacebookAccountRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportRecentFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportFacebookStartGameResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportRecentFacebookStartGameResponseParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "ReportInterface", name = "uggame-service-security", path = "${server.servlet.context-path}")
public interface ReportInterface {

    String MAPPING = "security/report/";

    @ApiOperation(value = "上报安全信息-facebook授权启动游戏")
    @PostMapping(value = MAPPING + "facebook/start-game")
    WebApiResponse<ReportFacebookStartGameResponseParam> startFacebookGame(@RequestBody @Validated ReportFacebookStartGameRequestParam param);


    @ApiOperation(value = "查询安全信息-最近通过facebook授权启动游戏的记录")
    @PostMapping(value = MAPPING + "facebook/start-game/recent")
    WebApiResponse<ReportRecentFacebookStartGameResponseParam> queryRecentStartGameLog(@RequestBody @Validated ReportRecentFacebookStartGameRequestParam param);

    @ApiOperation(value = "上报安全信息-facebook授权设备列表退出结果")
    @PostMapping(value = MAPPING + "facebook/account/quit")
    WebApiResponse<BaseResponseParam> quitFacebookAccount(@RequestBody @Validated ReportQuitFacebookAccountRequestParam param);
}
