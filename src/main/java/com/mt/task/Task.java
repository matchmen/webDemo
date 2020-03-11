package com.mt.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * author: liqm
 * 2020-02-24
 */
@Slf4j
@Component
public class Task {

    @Scheduled(cron = "0/5 * * * * ?")
    public void execute() {
        log.info("任务执行,时间:{}", LocalDateTime.now());
    }
}
