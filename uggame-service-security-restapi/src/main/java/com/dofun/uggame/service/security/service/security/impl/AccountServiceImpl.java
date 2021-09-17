package com.dofun.uggame.service.security.service.security.impl;

import com.dofun.uggame.common.util.BeanMapperUtil;
import com.dofun.uggame.common.util.HttpSQSUtil;
import com.dofun.uggame.common.util.RC4Util;
import com.dofun.uggame.framework.core.id.IdUtil;
import com.dofun.uggame.framework.mysql.service.impl.BaseServiceImpl;
import com.dofun.uggame.service.security.clientapi.enums.StatusEnum;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountQueryGarenaChangePasswordListRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountReceiveGarenaChangePasswordRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.request.AccountSubmitResultForGarenaPasswordChangeRequestParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountQueryGarenaChangePasswordListResponseParam;
import com.dofun.uggame.service.security.constants.HttPSQSConstants;
import com.dofun.uggame.service.security.entity.AccountEntity;
import com.dofun.uggame.service.security.mapper.AccountMapper;
import com.dofun.uggame.service.security.service.security.AccountService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AccountServiceImpl extends BaseServiceImpl<AccountEntity, AccountMapper> implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private IdUtil idUtil;

    @Value("${httpsqs.ip}")
    private String ip;

    @Value("${httpsqs.port}")
    private Integer port;

    @Value("${httpsqs.auth}")
    private String auth;

    @Value("${encryption.key}")
    private String encryptionKey;

    @Override
    public void receiveGarenaChangePassword(AccountReceiveGarenaChangePasswordRequestParam param) {
        AccountEntity accountEntityForSelect = AccountEntity.builder().orderId(param.getOrderId()).build();
        AccountEntity existAccountEntity = accountMapper.selectOne(accountEntityForSelect);
        if (existAccountEntity == null) {
            log.info("{},不存在,新增记录。", param.getOrderId());
            AccountEntity accountEntityForInsert = AccountEntity.builder().id(idUtil.next()).build();
            BeanMapperUtil.copyProperties(param, accountEntityForInsert);
            accountEntityForInsert.setUpdateTime(new Date());
            accountEntityForInsert.setCreateTime(new Date());
            accountEntityForInsert.setStatus(StatusEnum.WAIT.getCode());
            int insertResult = accountMapper.insert(accountEntityForInsert);
            log.info("insertResult:{}", insertResult);
        } else {
            log.info("{},已经存在,更新记录。", param.getOrderId());
            BeanMapperUtil.copyProperties(param, existAccountEntity);
            existAccountEntity.setUpdateTime(new Date());
            //不管之前的状态值，再次上报改密，就需要再次处理
            existAccountEntity.setStatus(StatusEnum.WAIT.getCode());
            int updateResult = accountMapper.updateByPrimaryKeySelective(existAccountEntity);
            log.info("updateResult:{}", updateResult);
        }
    }

    @Override
    public void submitResultForGarenaPasswordChange(AccountSubmitResultForGarenaPasswordChangeRequestParam param) {
        //只有待处理以及处理失败的，才需要更新状态
        if (Objects.equals(param.getStatus(), StatusEnum.SUCCESS.getCode()) || Objects.equals(param.getStatus(), StatusEnum.FAILED.getCode())) {
            AccountEntity values = AccountEntity.builder()
                    .status(param.getStatus())
                    //明文转密文
                    .garenaPassword(RC4Util.encrypt(param.getGarenaPassword(), encryptionKey)).build();
            Example where = Example.builder(AccountEntity.class).build();
            where.createCriteria().andEqualTo("hao_id", param.getHaoId());
            int updateResult = accountMapper.updateByExampleSelective(values, where);
            log.info("updateResult:{}", updateResult);
        }
        if (Objects.equals(param.getStatus(), StatusEnum.SUCCESS.getCode())) {
            sendHTTPSQSMessage(param);
        }
    }

    private void sendHTTPSQSMessage(AccountSubmitResultForGarenaPasswordChangeRequestParam param) {
        log.info("开始发送HTTPSQS消息");
        HttpSQSUtil.put(ip, port, auth, HttPSQSConstants.QUEUE_DEFINE_NOTIFY_PHP_GARENA_PASSWORD_CHANGE_SUCCESS, param.toString());
    }

    @Override
    public AccountQueryGarenaChangePasswordListResponseParam queryNeedChangePasswordGarena(AccountQueryGarenaChangePasswordListRequestParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<AccountEntity> entityList = accountMapper.queryNeedChangePasswordGarena(param);
        if (entityList == null) {
            return null;
        }
        AccountQueryGarenaChangePasswordListResponseParam responseParam = AccountQueryGarenaChangePasswordListResponseParam.builder().build();
        List<AccountQueryGarenaChangePasswordListResponseParam.AccountQueryGarenaChangePasswordItemResponseParam> items = new ArrayList<>(entityList.size());
        entityList.forEach(accountEntity -> {
            AccountQueryGarenaChangePasswordListResponseParam.AccountQueryGarenaChangePasswordItemResponseParam item = AccountQueryGarenaChangePasswordListResponseParam.AccountQueryGarenaChangePasswordItemResponseParam.builder().build();
            BeanMapperUtil.copyProperties(accountEntity, item);
            //密文转明文
            item.setGarenaPassword(RC4Util.decry(item.getGarenaPassword(), encryptionKey));
            item.setGarenaAccount(RC4Util.decry(item.getGarenaAccount(), encryptionKey));
            item.setGarenaKey(RC4Util.decry(item.getGarenaKey(), encryptionKey));
            items.add(item);
        });
        responseParam.setItem(items);
        return responseParam;
    }
}
