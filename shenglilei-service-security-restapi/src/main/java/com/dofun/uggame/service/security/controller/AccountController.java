package com.dofun.uggame.service.security.controller;

import com.dofun.uggame.framework.common.base.BaseResponseParam;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.service.security.clientapi.interfaces.AccountInterface;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountLoginForGarenaChangePasswordRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountQueryGarenaChangePasswordListRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountSubmitResultForGarenaPasswordChangeRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountLoginForGarenaChangePasswordResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountQueryGarenaChangePasswordListResponseParam;
import com.dofun.uggame.service.security.service.security.AccountService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "账号服务")
@RestController
@Slf4j
public class AccountController implements AccountInterface {

    @Resource
    private AccountService accountService;

    @Override
    public WebApiResponse<AccountLoginForGarenaChangePasswordResponseParam> loginForChangePasswordGarena(@RequestBody @Validated AccountLoginForGarenaChangePasswordRequestParam param) {
        AccountLoginForGarenaChangePasswordResponseParam responseParam = accountService.loginForChangePasswordGarena(param);
        return WebApiResponse.success(responseParam);
    }

    @Override
    public WebApiResponse<AccountQueryGarenaChangePasswordListResponseParam> queryNeedChangePasswordGarena(@RequestBody @Validated AccountQueryGarenaChangePasswordListRequestParam param) {
        accountService.checkAccessToken(param.getAccessToken());
        AccountQueryGarenaChangePasswordListResponseParam responseParam = accountService.queryNeedChangePasswordGarena(param);
        return WebApiResponse.success(responseParam);
    }

    @Override
    public WebApiResponse<BaseResponseParam> submitResultForGarenaPasswordChange(@RequestBody @Validated AccountSubmitResultForGarenaPasswordChangeRequestParam param) {
        if (!(param.getStatus() == 10 || param.getStatus() == 20)) {
            throw new IllegalArgumentException("状态不正确:[10、20]");
        }
        accountService.checkAccessToken(param.getAccessToken());
        accountService.submitResultForGarenaPasswordChange(param);
        return WebApiResponse.success(BaseResponseParam.empty());
    }
}
