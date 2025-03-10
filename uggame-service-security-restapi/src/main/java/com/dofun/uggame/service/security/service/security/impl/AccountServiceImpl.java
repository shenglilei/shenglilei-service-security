package com.dofun.uggame.service.security.service.security.impl;

import com.dofun.uggame.common.util.BeanMapperUtil;
import com.dofun.uggame.common.util.RC4Util;
import com.dofun.uggame.framework.common.error.impl.CommonError;
import com.dofun.uggame.framework.common.exception.BusinessException;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.framework.mysql.service.impl.BaseServiceImpl;
import com.dofun.uggame.framework.redis.service.RedisService;
import com.dofun.uggame.service.gamecenter.clientapi.interfaces.AccountSeatInterface;
import com.dofun.uggame.service.gamecenter.clientapi.pojo.request.AccountSeatUpdateRequestParam;
import com.dofun.uggame.service.gamecenter.clientapi.pojo.response.AccountSeatUpdateResponseParam;
import com.dofun.uggame.service.id.clientapi.interfaces.IdInterface;
import com.dofun.uggame.service.id.clientapi.pojo.response.IdResponseParam;
import com.dofun.uggame.service.security.clientapi.enums.StatusEnum;
import com.dofun.uggame.service.security.clientapi.pojo.request.*;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountLoginForGarenaChangePasswordResponseParam;
import com.dofun.uggame.service.security.clientapi.pojo.response.AccountQueryGarenaChangePasswordListResponseParam;
import com.dofun.uggame.service.security.entity.AccountEntity;
import com.dofun.uggame.service.security.mapper.AccountMapper;
import com.dofun.uggame.service.security.service.security.AccountService;
import com.dofun.uggame.service.security.service.wechat.WechatService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AccountServiceImpl extends BaseServiceImpl<AccountEntity, AccountMapper> implements AccountService {

    private static final String usernameKey = "uggame:auth:token:garena:change:password:clientType:";
    private static final String tokenKey = "uggame:auth:token:garena:change:password:token:";
    private static final String garenaLatestConnectTimeKey = "uggame:garena:latestConnectTime";
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IdInterface idInterface;
    @Autowired
    private RedisService redisService;
    @Autowired
    private WechatService wechatService;
    @Autowired
    private AccountSeatInterface accountSeatInterface;

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
    @Value("${spring.profiles.active}")
    private String active;

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
            WebApiResponse<IdResponseParam> idResponseParamWebApiResponse = idInterface.next();
            if (idResponseParamWebApiResponse.isSuccessAndHasContent()) {
                log.info("id:{}", idResponseParamWebApiResponse.getData().getId());
            } else {
                log.error("idInterface.next 调用失败.");
            }
            accessToken = DigestUtils.md5Hex(String.valueOf(idResponseParamWebApiResponse.getData().getId()));
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
            redisService.expire(myUsernameKey, ticketTime, TimeUnit.SECONDS);
            redisService.expire(myTokenKey, ticketTime, TimeUnit.SECONDS);
        }
        return AccountLoginForGarenaChangePasswordResponseParam.builder().accessToken(accessToken).expireAt(new Date(System.currentTimeMillis() + ticketTime)).build();
    }

    @Override
    public void checkAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new BusinessException(CommonError.ILLEGAL_PARAMETER);
        }
        String myTokenKey = tokenKey + accessToken;
        AccountLoginForGarenaChangePasswordRequestParam param = redisService.getObject(myTokenKey, AccountLoginForGarenaChangePasswordRequestParam.class);
        if (param == null) {
            throw new BusinessException(CommonError.UN_AUTHORIZED);
        }
        String myUsernameKey = usernameKey + param.getClientType() + ":" + "username:" + param.getUsername();
        redisService.expire(myUsernameKey, ticketTime, TimeUnit.SECONDS);
        redisService.expire(myTokenKey, ticketTime, TimeUnit.SECONDS);
    }

    @Override
    public void receiveGarenaChangePassword(AccountReceiveGarenaChangePasswordRequestParam param) {
        AccountEntity accountEntityForSelect = AccountEntity.builder().orderId(param.getOrderId()).build();
        AccountEntity existAccountEntity = accountMapper.selectOne(accountEntityForSelect);
        if (existAccountEntity == null) {
            log.info("{},不存在,新增记录。", param.getOrderId());
            WebApiResponse<IdResponseParam> idResponseParamWebApiResponse = idInterface.next();
            if (idResponseParamWebApiResponse.isSuccessAndHasContent()) {
                log.info("id:{}", idResponseParamWebApiResponse.getData().getId());
            } else {
                log.error("idInterface.next 调用失败.");
            }
            AccountEntity accountEntityForInsert = AccountEntity.builder().id(idResponseParamWebApiResponse.getData().getId()).build();
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
        String activeProfiles = "";
        // 判断当前环境是否是正式环境
        if ("prod".equals(active)) {
            activeProfiles = "【生产环境】";
        } else {
            activeProfiles = "【测试环境】";
        }
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
                // 改密失败发送企业微信机器人推送
                WechatRobotRequestParam wechatRobotParam = new WechatRobotRequestParam();
                wechatRobotParam.setContent(activeProfiles + "Garena改密失败，orderId不存在于数据库，入参：" + param);
                wechatService.sendWechatRobotTextMsg(wechatRobotParam);
                throw new IllegalArgumentException("orderId不存在");
            }
            //不是待处理状态的就跳过
            if (!existAccountEntity.getStatus().equals(StatusEnum.WAIT.getCode())) {
                log.info("{},状态:{},不需要重复处理.", param.getOrderId(), existAccountEntity.getStatus());
                return;
            }
            if (!existAccountEntity.getGarenaAccount().equals(param.getGarenaAccount())) {
                log.info("账号信息不一致,数据库:{},入参:{},", existAccountEntity.getGarenaAccount(), param.getGarenaAccount());
                // 改密失败发送企业微信机器人推送
                WechatRobotRequestParam wechatRobotParam = new WechatRobotRequestParam();
                wechatRobotParam.setContent(activeProfiles + "Garena改密失败，Garena账户不一致，入参：" + param);
                wechatService.sendWechatRobotTextMsg(wechatRobotParam);
                throw new IllegalArgumentException("orderId不存在");
            }
            if (!existAccountEntity.getHaoId().equals(param.getHaoId())) {
                log.info("货架Id不一致,数据库:{},入参:{},", existAccountEntity.getHaoId(), param.getHaoId());
                // 改密失败发送企业微信机器人推送
                WechatRobotRequestParam wechatRobotParam = new WechatRobotRequestParam();
                wechatRobotParam.setContent(activeProfiles + "Garena改密失败，货架Id不一致，入参：" + param);
                wechatService.sendWechatRobotTextMsg(wechatRobotParam);
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
        } else {
            // 改密失败发送企业微信机器人推送
            WechatRobotRequestParam wechatRobotParam = new WechatRobotRequestParam();
            wechatRobotParam.setContent(activeProfiles + "Garena改密失败，入参：" + param + "。请开发检查是否有误。");
            wechatService.sendWechatRobotTextMsg(wechatRobotParam);
        }
        if (Objects.equals(param.getStatus(), StatusEnum.SUCCESS.getCode())) {
            //明文转密文
            param.setGarenaPassword(RC4Util.encrypt(param.getGarenaPassword(), encryptionKey));
            WebApiResponse<AccountSeatUpdateResponseParam> accountSeatUpdate = accountSeatInterface.update(new AccountSeatUpdateRequestParam().setId(param.getHaoId()).setGarenaPassword(param.getGarenaPassword()));
            if (accountSeatUpdate.isFail() || !(accountSeatUpdate.getData().getUpdateResult() > 0)) {
                // 改密失败发送企业微信机器人推送
                WechatRobotRequestParam wechatRobotParam = new WechatRobotRequestParam();
                wechatRobotParam.setContent(activeProfiles + "Garena改密无更新，入参：" + param + "。请开发检查是否有误。");
                wechatService.sendWechatRobotTextMsg(wechatRobotParam);
            }
        }
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
        redisService.set(garenaLatestConnectTimeKey, System.currentTimeMillis());
        return responseParam;
    }

    @Override
    public void checkGarena() {
        String latestTime = redisService.get(garenaLatestConnectTimeKey);
        if (StringUtils.isEmpty(latestTime) || System.currentTimeMillis() - Long.parseLong(latestTime) > 60000) {
            WechatRobotRequestParam param = new WechatRobotRequestParam();
            // 判断当前环境是否是正式环境
            if ("prod".equals(active)) {
                param.setContent("【生产环境】Garena已超过1分钟未拉取改密数据，请相关同事注意。");
            } else {
                param.setContent("【测试环境】Garena已超过1分钟未拉取改密数据，请相关同事注意。");
            }
            wechatService.sendWechatRobotTextMsg(param);
        }
    }

}
