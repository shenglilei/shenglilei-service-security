package com.dofun.uggame.service.security.clientapi.interfaces;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountLoginForGarenaChangePasswordRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountQueryGarenaChangePasswordListRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountSubmitResultForGarenaPasswordChangeRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountLoginForGarenaChangePasswordResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountQueryGarenaChangePasswordListResponseParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "AccountInterface", name = "uggame-service-security", path = "${server.servlet.context-path}")
public interface AccountInterface {

    String MAPPING = "security/account/";

    @ApiOperation(value = "账号服务-登录")
    @PostMapping(value = MAPPING + "garena/password/change/login")
    WebApiResponse<AccountLoginForGarenaChangePasswordResponseParam> loginForChangePasswordGarena(@RequestBody @Validated AccountLoginForGarenaChangePasswordRequestParam param);

    @ApiOperation(value = "账号服务-查询需要修改garena密码的账号列表")
    @PostMapping(value = MAPPING + "garena/password/change/list")
    WebApiResponse<AccountQueryGarenaChangePasswordListResponseParam> queryNeedChangePasswordGarena(@RequestBody @Validated AccountQueryGarenaChangePasswordListRequestParam param);

    @ApiOperation(value = "账号服务-提交garena密码的修改结果")
    @PostMapping(value = MAPPING + "garena/password/change")
    WebApiResponse<BaseResponseParam> submitResultForGarenaPasswordChange(@RequestBody @Validated AccountSubmitResultForGarenaPasswordChangeRequestParam param);
}
