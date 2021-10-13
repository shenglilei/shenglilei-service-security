package com.dofun.uggame.service.security.controller;

import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.service.security.clientapi.interfaces.SecurityCommonInterface;
import com.dofun.uggame.service.security.clientapi.pojo.request.CommonCheckCookieRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.CommonCheckCookieResponseParam;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * SecurityCommonController
 *
 * @author Achin
 * @since 2021-10-13
 */
@Api(tags = "公共模块")
@RestController
@Slf4j
public class SecurityCommonController implements SecurityCommonInterface {

    @Override
    public WebApiResponse<CommonCheckCookieResponseParam> checkCookie(CommonCheckCookieRequestParam param) {
        // TODO 待对接nodejs相关内容
        return WebApiResponse.success(new CommonCheckCookieResponseParam().setStatus(1));
    }

}
