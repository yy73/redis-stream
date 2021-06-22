package com.yy.redis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ywl
 * @Date 2021/6/21 15:05
 * @Description
 */
@RestController
public class ConsumerController {

    @GetMapping("/demo")
    public String demo() {
        return "hell consumer";
    }
}
