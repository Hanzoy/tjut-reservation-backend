package com.hanzoy.tjutreservation.controller;

import com.hanzoy.tjutreservation.aop.API;
import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.LoginAuthParam;
import com.hanzoy.tjutreservation.pojo.dto.param.LoginParam;
import com.hanzoy.tjutreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @API(value = "login", remark = "登陆接口")
    public CommonResult login(@Validated @RequestBody LoginParam param){
        return userService.login(param);
    }

    @PostMapping("/loginAuth")
    @API(value = "loginAuth", remark = "第一次登陆授权并完善信息")
    public CommonResult loginAuth(@Validated @RequestBody LoginAuthParam param){
        return userService.loginAuth(param);
    }
}
