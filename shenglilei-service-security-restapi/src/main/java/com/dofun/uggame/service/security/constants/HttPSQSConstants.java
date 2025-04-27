package com.dofun.uggame.service.security.constants;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/16
 * Time:17:26
 */
public interface HttPSQSConstants {
    /**
     * java发给php的消息-garena密码修改成功
     */
    String QUEUE_DEFINE_NOTIFY_PHP_GARENA_PASSWORD_CHANGE_SUCCESS = "queue.java.to.php:notify.garena.password.change.success";

    /**
     * php发给java的消息-订单需要修改garena密码
     */
    String QUEUE_DEFINE_NOTIFY_JAVA_ORDER_NEED_CHANGE_GARENA_PASSWORD = "queue.php.to.java:notify.need.garena.password.change";
}
