package com.dofun.uggame.service.security.service.security;

import com.dofun.uggame.framework.mysql.service.BaseService;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountLoginForGarenaChangePasswordRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountQueryGarenaChangePasswordListRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountReceiveGarenaChangePasswordRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountSubmitResultForGarenaPasswordChangeRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountLoginForGarenaChangePasswordResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountQueryGarenaChangePasswordListResponseParam;
import com.dofun.uggame.service.security.entity.AccountEntity;

public interface AccountService extends BaseService<AccountEntity> {
    void receiveGarenaChangePassword(AccountReceiveGarenaChangePasswordRequestParam param);

    void submitResultForGarenaPasswordChange(AccountSubmitResultForGarenaPasswordChangeRequestParam param);

    AccountQueryGarenaChangePasswordListResponseParam queryNeedChangePasswordGarena(AccountQueryGarenaChangePasswordListRequestParam param);

    AccountLoginForGarenaChangePasswordResponseParam loginForChangePasswordGarena(AccountLoginForGarenaChangePasswordRequestParam param);

    void checkAccessToken(String accessToken);

    void checkGarena();
}
