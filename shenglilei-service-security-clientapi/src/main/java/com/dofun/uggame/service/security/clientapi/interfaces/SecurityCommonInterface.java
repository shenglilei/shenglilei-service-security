package com.dofun.uggame.service.security.clientapi.interfaces;

import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.service.security.clientapi.pojo.request.CommonCheckCookieRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.CommonCheckCookieResponseParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SecurityCommonInterface
 *
 * @author Achin
 * @since 2021-10-13
 */
@FeignClient(contextId = "SecurityCommonInterface", name = "uggame-service-security", path = "${server.servlet.context-path}")
public interface SecurityCommonInterface {

    String MAPPING = "/security/common/";

    @ApiOperation(value = "安全-从nodejs校验cookie是否有效")
    @PostMapping(value = MAPPING + "check/cookie")
    WebApiResponse<CommonCheckCookieResponseParam> checkCookie(@RequestBody @Validated CommonCheckCookieRequestParam param);

}
