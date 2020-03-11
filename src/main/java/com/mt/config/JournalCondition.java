package com.mt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * author: liqm
 * 2020-03-09
 */
@Slf4j
public class JournalCondition implements Condition {

    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        String flg = conditionContext.getEnvironment().getProperty("enabledJournalTracedId");

        log.debug("是否启动日志跟踪ID：{}", flg);

        return "true".equals(flg);
    }
}