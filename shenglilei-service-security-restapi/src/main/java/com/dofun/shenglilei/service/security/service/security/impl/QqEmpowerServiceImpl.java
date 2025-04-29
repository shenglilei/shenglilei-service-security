package com.dofun.shenglilei.service.security.service.security.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.dofun.shenglilei.framework.common.base.BasePageRequestParam;
import com.dofun.shenglilei.framework.common.base.BasePageResponseParam;
import com.dofun.shenglilei.framework.mysql.service.impl.BaseServiceImpl;
import com.dofun.shenglilei.framework.mysql.util.PageUtils;
import com.dofun.shenglilei.service.security.clientapi.pojo.response.TestResponseParam;
import com.dofun.shenglilei.service.security.entity.QqEmpowerEntity;
import com.dofun.shenglilei.service.security.mapper.QqEmpowerMapper;
import com.dofun.shenglilei.service.security.service.security.QqEmpowerService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 服务实现类
 *
 * @author Sheng
 * @since 2025-04-29
 */
@Service
@Slf4j
public class QqEmpowerServiceImpl extends BaseServiceImpl<QqEmpowerEntity, QqEmpowerMapper> implements QqEmpowerService {

    @Override
    public BasePageResponseParam<TestResponseParam> page(BasePageRequestParam basePageRequestParam) {
        PageHelper.startPage(basePageRequestParam.getPageNum(), basePageRequestParam.getPageSize());

        List<QqEmpowerEntity> list = new LambdaQueryChainWrapper<>(mapper)
                .list();
        log.info("list===={}", JSONObject.toJSONString(list));
        return PageUtils.asPageResponse(PageUtils.buildPageResult(list, TestResponseParam.class));
    }
}