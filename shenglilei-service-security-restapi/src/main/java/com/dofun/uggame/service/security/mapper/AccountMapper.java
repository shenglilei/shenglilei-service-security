package com.dofun.uggame.service.security.mapper;

import com.dofun.uggame.framework.mysql.mappers.BaseMapper;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountQueryGarenaChangePasswordListRequestParam;
import com.dofun.uggame.service.security.entity.AccountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper extends BaseMapper<AccountEntity> {
    List<AccountEntity> queryNeedChangePasswordGarena(@Param("param") AccountQueryGarenaChangePasswordListRequestParam param);
}