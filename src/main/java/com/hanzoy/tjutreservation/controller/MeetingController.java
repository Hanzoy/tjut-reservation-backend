package com.hanzoy.tjutreservation.controller;

import com.hanzoy.tjutreservation.aop.API;
import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.GetMyReservationsParam;
import com.hanzoy.tjutreservation.pojo.dto.param.PostReservationParam;
import com.hanzoy.tjutreservation.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingController {

    @Autowired
    MeetingService meetingService;

    @PostMapping("/postReservation")
    @API(value = "postReservation", remark = "会议室申请接口")
    public CommonResult postReservation(@Validated @RequestBody PostReservationParam param){
        return meetingService.postReservation(param);
    }

    @PostMapping("/getMyReservations")
    @API(value = "getMyReservations", remark = "获取用户指定月份与自己相关的会议")
    public CommonResult getMyReservations(@Validated @RequestBody GetMyReservationsParam param){
        return meetingService.getMyReservations(param);
    }
}
