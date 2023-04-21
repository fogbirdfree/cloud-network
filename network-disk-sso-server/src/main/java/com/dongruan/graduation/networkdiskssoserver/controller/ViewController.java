package com.dongruan.graduation.networkdiskssoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: duyubo
 * @date: 2021年02月25日, 0025 09:39
 * @description:
 */

@Controller
public class ViewController {
    @GetMapping(value = "/register")
    public String register() {
        return "register";
    }
    @GetMapping(value = "/getPass")
    public String forget() {
        return "forgetpass";
    }

}
