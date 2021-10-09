package com.dofun.uggame.service.security.service.security.impl;

import com.dofun.uggame.common.util.BeanMapperUtil;
import com.dofun.uggame.common.util.RC4Util;
import com.dofun.uggame.framework.common.enums.ReqEndPointEnum;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.framework.mysql.service.impl.BaseServiceImpl;
import com.dofun.uggame.service.id.clientapi.interfaces.IdInterface;
import com.dofun.uggame.service.id.clientapi.pojo.response.IdResponseParam;
import com.dofun.uggame.service.security.clientapi.enums.StatusEnum;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportQuitFacebookAccountRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.ReportRecentFacebookStartGameRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportFacebookStartGameResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.ReportRecentFacebookStartGameResponseParam;
import com.dofun.uggame.service.security.entity.ReportEntity;
import com.dofun.uggame.service.security.mapper.ReportMapper;
import com.dofun.uggame.service.security.service.security.ReportService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ReportServiceImpl extends BaseServiceImpl<ReportEntity, ReportMapper> implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private IdInterface idInterface;

    @Value("${encryption.key}")
    private String encryptionKey;

    @Override
    public ReportFacebookStartGameResponseParam startFacebookGame(ReportFacebookStartGameRequestParam param) {
        ReportEntity reportEntityForSelect = ReportEntity.builder().orderId(param.getOrderId()).build();
        ReportEntity existReportEntity = reportMapper.selectOne(reportEntityForSelect);
        if (existReportEntity == null) {
            log.info("{},不存在,新增记录。", param.getOrderId());
            WebApiResponse<IdResponseParam> idResponseParamWebApiResponse = idInterface.next();
            if (idResponseParamWebApiResponse.isSuccessAndHasContent()) {
                log.info("id:{}", idResponseParamWebApiResponse.getData().getId());
            } else {
                log.error("idInterface.next 调用失败.");
            }
            ReportEntity reportEntityForInsert = ReportEntity.builder().id(idResponseParamWebApiResponse.getData().getId()).build();
            BeanMapperUtil.copyProperties(param, reportEntityForInsert);
            reportEntityForInsert.setUpdateTime(new Date());
            reportEntityForInsert.setCreateTime(new Date());
            reportEntityForInsert.setStatus(StatusEnum.WAIT.getCode());
            reportEntityForInsert.setStatusUpdateSource(StringUtils.isEmpty(param.getStatusUpdateSource()) ? ReqEndPointEnum.ANDROID_APP.getName() : param.getStatusUpdateSource());
            int insertResult = reportMapper.insert(reportEntityForInsert);
            log.info("insertResult:{}", insertResult);
            return ReportFacebookStartGameResponseParam.builder().id(reportEntityForInsert.getId()).build();
        } else {
            log.info("{},已经存在,更新记录。", param.getOrderId());
            BeanMapperUtil.copyProperties(param, existReportEntity);
            existReportEntity.setUpdateTime(new Date());
            //不管之前的状态值，重新授权登录，就需要再次处理
            existReportEntity.setStatus(StatusEnum.WAIT.getCode());
            int updateResult = reportMapper.updateByPrimaryKeySelective(existReportEntity);
            log.info("updateResult:{}", updateResult);
            return ReportFacebookStartGameResponseParam.builder().id(existReportEntity.getId()).build();
        }
    }

    @Override
    public ReportRecentFacebookStartGameResponseParam queryRecentStartGameLog(ReportRecentFacebookStartGameRequestParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<ReportEntity> entityList = reportMapper.queryRecentStartGameLog(param);
        if (entityList == null) {
            return null;
        }
        ReportRecentFacebookStartGameResponseParam responseParam = ReportRecentFacebookStartGameResponseParam.builder().build();
        List<ReportRecentFacebookStartGameResponseParam.ReportRecentFacebookStartGameItemResponseParam> items = new ArrayList<>(entityList.size());
        entityList.forEach(reportEntity -> {
            ReportRecentFacebookStartGameResponseParam.ReportRecentFacebookStartGameItemResponseParam item = ReportRecentFacebookStartGameResponseParam.ReportRecentFacebookStartGameItemResponseParam.builder().build();
            BeanMapperUtil.copyProperties(reportEntity, item);
            //密文转明文
            item.setFacebookAccount(RC4Util.decry(item.getFacebookAccount(), encryptionKey));
            item.setFacebookPassword(RC4Util.decry(item.getFacebookPassword(), encryptionKey));
            items.add(item);
        });
        responseParam.setItem(items);
        return responseParam;
    }

    @Override
    public void quitFacebookAccount(ReportQuitFacebookAccountRequestParam param) {
        ReportEntity existReportEntity = reportMapper.selectByPrimaryKey(param.getId());
        if (existReportEntity != null) {
            //只有待处理以及处理失败的，才需要更新状态
            if (Objects.equals(existReportEntity.getStatus(), StatusEnum.WAIT.getCode()) || Objects.equals(existReportEntity.getStatus(), StatusEnum.FAILED.getCode())) {
                existReportEntity.setStatus(param.getStatus());
                existReportEntity.setUpdateTime(new Date());
                existReportEntity.setStatusUpdateSource(param.getStatusUpdateSource());
                int updateResult = reportMapper.updateByPrimaryKey(existReportEntity);
                log.info("updateResult:{}", updateResult);
            }
        }
    }
}
