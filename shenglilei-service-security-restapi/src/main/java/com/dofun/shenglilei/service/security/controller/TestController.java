package com.dofun.shenglilei.service.security.controller;

import com.dofun.shenglilei.framework.common.base.BasePageRequestParam;
import com.dofun.shenglilei.framework.common.base.BasePageResponseParam;
import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import com.dofun.shenglilei.framework.common.response.WebApiResponse;
import com.dofun.shenglilei.service.security.clientapi.interfaces.TestInterface;
import com.dofun.shenglilei.service.security.clientapi.pojo.response.TestResponseParam;
import com.dofun.shenglilei.service.security.service.security.QqEmpowerService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "测试功能")
@RestController
@Slf4j
public class TestController implements TestInterface {

    @Resource
    private QqEmpowerService qqEmpowerService;

    @Override
    public WebApiResponse<BasePageResponseParam<TestResponseParam>> page(BasePageRequestParam basePageRequestParam) {

        return WebApiResponse.success(qqEmpowerService.page(basePageRequestParam));
    }
}
