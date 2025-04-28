package com.dofun.shenglilei.service.security.controller;

import com.dofun.shenglilei.framework.common.base.BasePageResponseParam;
import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import com.dofun.shenglilei.framework.common.response.WebApiResponse;
import com.dofun.shenglilei.service.security.clientapi.interfaces.TestInterface;
import com.dofun.shenglilei.service.security.clientapi.pojo.response.TestResponseParam;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试功能")
@RestController
@Slf4j
public class TestController implements TestInterface {


    @Override
    public WebApiResponse<BasePageResponseParam<TestResponseParam>> page(BaseRequestParam baseRequestParam) {
        log.error("test====");
        return WebApiResponse.success(new BasePageResponseParam<>());
    }
}
