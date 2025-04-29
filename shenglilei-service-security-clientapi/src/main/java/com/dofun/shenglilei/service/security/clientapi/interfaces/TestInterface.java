package com.dofun.shenglilei.service.security.clientapi.interfaces;

import com.dofun.shenglilei.framework.common.base.BasePageRequestParam;
import com.dofun.shenglilei.framework.common.base.BasePageResponseParam;
import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import com.dofun.shenglilei.framework.common.response.WebApiResponse;
import com.dofun.shenglilei.service.security.clientapi.pojo.response.TestResponseParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "TestInterface", name = "shenglilei-service-financecenter", path = "${server.servlet.context-path}")
public interface TestInterface {

    String MAPPING = "/security/test/";

    @ApiOperation(value = "分页Test")
    @PostMapping(value = MAPPING + "page")
    WebApiResponse<BasePageResponseParam<TestResponseParam>> page(@RequestBody @Validated BasePageRequestParam basePageRequestParam);


}
