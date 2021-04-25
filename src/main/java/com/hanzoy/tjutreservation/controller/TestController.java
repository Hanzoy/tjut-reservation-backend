package com.hanzoy.tjutreservation.controller;

import com.hanzoy.tjutreservation.aop.API;
import com.hanzoy.tjutreservation.service.SchedulerService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    SchedulerService schedulerService;

    @PostMapping("/test")
    @API(value = "test",remark = "测试")
    public void test(@Param("value") String value){

    }
}
