package com.dofun.uggame.service.account;

import com.dofun.uggame.common.util.AssertUtils;
import com.dofun.uggame.common.util.RandomUtil;
import com.dofun.uggame.framework.redis.lock.RedisLock;
import com.dofun.uggame.framework.redis.service.RedisService;
import com.dofun.uggame.service.security.ServiceSecurityApplication;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2019/11/27
 * Time:15:49
 *
 * @author: steven
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceSecurityApplication.class)
public class RedisTest {

    @Autowired
    private RedisService redisService;

    @SneakyThrows
    @Test
    public void baseLine() {
        String key = RandomUtil.getMixed(16);
        String value = RandomUtil.getMixed(16);
        redisService.set(key, value);
        AssertUtils.isTrue(redisService.get(key).equals(value), "数据不一致");
        redisService.delete(key);
        AssertUtils.isTrue(redisService.get(key) == null, "数据删除失败");
        try (RedisLock lock = new RedisLock(redisService, RandomUtil.getMixed(16))) {
            Thread.sleep(2000);
        }
    }
}
