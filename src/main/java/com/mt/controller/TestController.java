package com.mt.controller;

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


    @PostMapping(value = "/hello")
    public String hello() {

        return "嗨！";
    }


}
