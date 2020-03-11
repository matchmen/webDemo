package com.mt.controller;

import com.mt.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * author: liqm
 * 2020-02-21
 */

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private HelloService helloService;

    @PostMapping(value = "/hello")
    public String hello() {

        return helloService.hello();
    }


}
