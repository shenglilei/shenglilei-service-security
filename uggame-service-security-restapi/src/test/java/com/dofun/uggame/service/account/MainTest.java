package com.dofun.uggame.service.account;

import com.dofun.uggame.service.security.ServiceSecurityApplication;
import com.dofun.uggame.service.security.entity.ReportEntity;
import com.dofun.uggame.service.security.service.security.ReportService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
public class MainTest {

    @Autowired
    private ReportService reportService;

    @Test
    public void baseLine() {
        selectAllOrder();
    }


    @Test
    public void selectAllOrder() {
        PageInfo<ReportEntity> orderPage = reportService.pageSelect(0, 100, null);
        List<ReportEntity> orderList = orderPage.getList();
        for (ReportEntity order : orderList) {
            log.info("order:{}", order.toString());
        }
    }
}
