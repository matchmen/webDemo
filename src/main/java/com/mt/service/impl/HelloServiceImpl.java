package com.mt.service.impl;

import com.mt.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * author: liqm
 * 2020-03-09
 */
@Service
@Slf4j
public class HelloServiceImpl implements HelloService {

    public String hello() {

        log.info("hello!");
        return "hai!";
    }
}
