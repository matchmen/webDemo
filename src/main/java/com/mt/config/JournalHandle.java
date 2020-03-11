package com.mt.config;

import com.mt.key.MySnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;


/**
 * author: liqm
 * 2020-03-09
 */
@Slf4j
@Component
@Conditional(JournalCondition.class)
@Aspect
public class JournalHandle {

    private String traceIdKey = "Trace-Id";

    private String traceIdPrefix = "Trace-Id-";

    @Autowired
    private MySnowFlake mySnowFlake;

    public JournalHandle() {
        log.debug("初始化日志跟踪处理器......");
    }

    @Pointcut("(within(com.mt.controller..*) && @annotation(org.springframework.web.bind.annotation.PostMapping)) " +
            "|| (within(com.mt.task..*) && @annotation(org.springframework.scheduling.annotation.Scheduled)) ")
    public void scanPackage() {
        log.debug("切入点");
    }

    @Before("JournalHandle.scanPackage()")
    public void before1() {
        MDC.put(traceIdKey, traceIdPrefix + mySnowFlake.generate());
        log.debug("加入日志号:{}",MDC.get(traceIdKey));
    }

    @After("JournalHandle.scanPackage()")
    public void after1() {
        log.debug("移除日志号:{}",MDC.get(traceIdKey));
        MDC.remove(traceIdKey);

    }

}
