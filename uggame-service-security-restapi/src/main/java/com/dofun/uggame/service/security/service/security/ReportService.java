package com.dofun.uggame.service.security.service.security;

import com.dofun.uggame.framework.mysql.service.BaseService;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportQuitFacebookAccountRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportRecentFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportFacebookStartGameResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportRecentFacebookStartGameResponseParam;
import com.dofun.uggame.service.security.entity.ReportEntity;

public interface ReportService extends BaseService<ReportEntity> {
    ReportFacebookStartGameResponseParam startFacebookGame(ReportFacebookStartGameRequestParam param);

    ReportRecentFacebookStartGameResponseParam queryRecentStartGameLog(ReportRecentFacebookStartGameRequestParam param);

    void quitFacebookAccount(ReportQuitFacebookAccountRequestParam param);
}
