package com.dofun.shenglilei.service.security.service.security;

import com.dofun.shenglilei.framework.common.base.BasePageRequestParam;
import com.dofun.shenglilei.framework.common.base.BasePageResponseParam;
import com.dofun.shenglilei.service.security.clientapi.pojo.response.TestResponseParam;

/**
 *  服务类
 *
 * @author Sheng
 * @since 2025-04-29
 */
public interface QqEmpowerService {

    BasePageResponseParam<TestResponseParam> page(BasePageRequestParam basePageRequestParam);

}