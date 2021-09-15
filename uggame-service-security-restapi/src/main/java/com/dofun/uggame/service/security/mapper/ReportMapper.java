package com.dofun.uggame.service.security.mapper;

import com.dofun.uggame.framework.mysql.mappers.BaseMapper;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportRecentFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.entity.ReportEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportMapper extends BaseMapper<ReportEntity> {
    List<ReportEntity> queryRecentStartGameLog(@Param("param") ReportRecentFacebookStartGameRequestParam param);
}