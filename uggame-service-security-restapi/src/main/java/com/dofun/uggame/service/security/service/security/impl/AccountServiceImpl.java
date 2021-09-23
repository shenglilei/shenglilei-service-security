package com.dofun.uggame.service.security.service.security.impl;

import com.alibaba.fastjson.JSON;
import com.dofun.uggame.common.util.BeanMapperUtil;
import com.dofun.uggame.common.util.HttpSQSUtil;
import com.dofun.uggame.common.util.RC4Util;
import com.dofun.uggame.framework.common.error.impl.CommonError;
import com.dofun.uggame.framework.common.exception.BusinessException;
import com.dofun.uggame.framework.core.id.IdUtil;
import com.dofun.uggame.framework.mysql.service.impl.BaseServiceImpl;
import com.dofun.uggame.framework.redis.service.RedisService;
import com.dofun.uggame.service.security.clientapi.enums.StatusEnum;
import com.dofun.uggame.service.security.clientapi.pojo.request.*;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountLoginForGarenaChangePasswordResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountQueryGarenaChangePasswordListResponseParam;
import com.dofun.uggame.service.security.constants.HttPSQSConstants;
import com.dofun.uggame.service.security.entity.AccountEntity;
import com.dofun.uggame.service.security.mapper.AccountMapper;
import com.dofun.uggame.service.security.service.security.AccountService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
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
public class AccountServiceImpl extends BaseServiceImpl<AccountEntity, AccountMapper> implements AccountService {

    private static final String usernameKey = "uggame:auth:token:garena:change:password:clientType:";
    private static final String tokenKey = "uggame:auth:token:garena:change:password:token:";
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IdUtil idUtil;
    @Autowired
    private RedisService redisService;
    @Value("${httpsqs.ip}")
    private String ip;
    @Value("${httpsqs.port}")
    private Integer port;
    @Value("${httpsqs.auth}")
    private String auth;
    @Value("${encryption.key}")
    private String encryptionKey;
    @Value("${client.type}")
    private String clientType;
    @Value("${client.auth.username}")
    private String authUsername;
    @Value("${client.auth.password}")
    private String authPassword;
    @Value("${client.auth.ticketTime}")
    private Long ticketTime;

    @Override
    public AccountLoginForGarenaChangePasswordResponseParam loginForChangePasswordGarena(AccountLoginForGarenaChangePasswordRequestParam param) {
        if (!param.getClientType().equals(clientType)) {
            throw new IllegalArgumentException("终端类型不正确");
        }
        if (!param.getPassword().equals(authPassword) || !param.getUsername().equals(authUsername)) {
            throw new IllegalArgumentException("账号或密码不正确");
        }
        String myUsernameKey = usernameKey + param.getClientType() + ":" + "username:" + param.getUsername();
        String accessToken = redisService.get(myUsernameKey);
        if (accessToken == null) {
            accessToken = DigestUtils.md5Hex(String.valueOf(idUtil.next()));
            String myTokenKey = tokenKey + accessToken;
            redisService.set(myUsernameKey, accessToken, ticketTime);
            redisService.set(myTokenKey, param, ticketTime);
        } else {
            String myTokenKey = tokenKey + accessToken;
            if (redisService.get(myTokenKey) == null) {
                log.info("数据不一致:{},{}", param, accessToken);
                redisService.delete(myUsernameKey);
                return loginForChangePasswordGarena(param);
            }
            redisService.expireAt(myUsernameKey, new Date(System.currentTimeMillis() + (ticketTime * 1000)));
            redisService.expireAt(myTokenKey, new Date(System.currentTimeMillis() + (ticketTime * 1000)));
        }
        return AccountLoginForGarenaChangePasswordResponseParam.builder().accessToken(accessToken).expireAt(new Date(System.currentTimeMillis() + ticketTime)).build();
    }

    @Override
    public void checkAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new BusinessException(CommonError.PARAMETER_ERROR);
        }
        String myTokenKey = tokenKey + accessToken;
        AccountLoginForGarenaChangePasswordRequestParam param = redisService.getObject(myTokenKey, AccountLoginForGarenaChangePasswordRequestParam.class);
        if (param == null) {
            throw new BusinessException(CommonError.UNAUTHORIZED);
        }
        String myUsernameKey = usernameKey + param.getClientType() + ":" + "username:" + param.getUsername();
        redisService.expireAt(myUsernameKey, new Date(System.currentTimeMillis() + (ticketTime * 1000)));
        redisService.expireAt(myTokenKey, new Date(System.currentTimeMillis() + (ticketTime * 1000)));
    }

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
            if (Objects.equals(param.getStatus(), StatusEnum.SUCCESS.getCode())) {
                if (param.getGarenaPassword() == null || param.getGarenaPassword().isEmpty()) {
                    throw new IllegalArgumentException("新密码不可以为空");
                }
            }
            AccountEntity accountEntityForSelect = AccountEntity.builder().orderId(param.getOrderId()).build();
            AccountEntity existAccountEntity = accountMapper.selectOne(accountEntityForSelect);
            if (existAccountEntity == null) {
                log.info("{},不存在。", param.getOrderId());
                throw new IllegalArgumentException("orderId不存在");
            }
            //不是待处理状态的就跳过
            if (!existAccountEntity.getStatus().equals(StatusEnum.WAIT.getCode())) {
                log.info("{},状态:{},不需要重复处理.", param.getOrderId(), existAccountEntity.getStatus());
                return;
            }
            if (!existAccountEntity.getGarenaAccount().equals(param.getGarenaAccount())) {
                log.info("账号信息不一致,数据库:{},入参:{},", existAccountEntity.getGarenaAccount(), param.getGarenaAccount());
                throw new IllegalArgumentException("orderId不存在");
            }
            if (!existAccountEntity.getHaoId().equals(param.getHaoId())) {
                log.info("货架Id不一致,数据库:{},入参:{},", existAccountEntity.getHaoId(), param.getHaoId());
                throw new IllegalArgumentException("orderId不存在");
            }
            if (Objects.equals(param.getStatus(), StatusEnum.SUCCESS.getCode())) {
                log.info("数据Id:{},订单号:{},货架Id:{},状态:{},旧密码:{},新密码:{}", existAccountEntity.getId(), param.getOrderId(), param.getHaoId(), param.getStatus(), RC4Util.decry(existAccountEntity.getGarenaPassword(), encryptionKey), param.getGarenaPassword());
            }
            BeanMapperUtil.copyProperties(param, existAccountEntity);
            //明文转密文
            existAccountEntity.setGarenaPassword(Objects.equals(param.getStatus(), StatusEnum.FAILED.getCode()) ? null : RC4Util.encrypt(param.getGarenaPassword(), encryptionKey));
            existAccountEntity.setUpdateTime(new Date());
            int updateResult = accountMapper.updateByPrimaryKeySelective(existAccountEntity);
            log.info("updateResult:{}", updateResult);
            if (updateResult > 0) {
                log.info("改密成功.");
            } else {
                log.error("改密失败.");
            }
        }
        if (Objects.equals(param.getStatus(), StatusEnum.SUCCESS.getCode())) {
            //明文转密文
            param.setGarenaPassword(RC4Util.encrypt(param.getGarenaPassword(), encryptionKey));
            sendHTTPSQSMessage(param);
        }
    }

    private void sendHTTPSQSMessage(AccountSubmitResultForGarenaPasswordChangeRequestParam param) {
        AccountSubmitResultForGarenaPasswordChange2PHPRequestParam phpRequestParam = new AccountSubmitResultForGarenaPasswordChange2PHPRequestParam();
        BeanMapperUtil.copyProperties(param, phpRequestParam);
        log.info("开始发送HTTPSQS消息:{}", JSON.toJSONString(phpRequestParam));
        HttpSQSUtil.put(ip, port, auth, HttPSQSConstants.QUEUE_DEFINE_NOTIFY_PHP_GARENA_PASSWORD_CHANGE_SUCCESS, JSON.toJSONString(phpRequestParam));
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
            items.add(item);
        });
        responseParam.setItem(items);
        return responseParam;
    }
}
