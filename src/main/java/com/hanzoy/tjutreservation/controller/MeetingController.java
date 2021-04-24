package com.hanzoy.tjutreservation.controller;

import com.hanzoy.tjutreservation.aop.API;
import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.*;
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
    public CommonResult postReservation(@Validated @RequestBody PostReservationParam param) {
        return meetingService.postReservation(param);
    }

    @PostMapping("/getMyReservations")
    @API(value = "getMyReservations", remark = "获取用户指定月份与自己相关的会议")
    public CommonResult getMyReservations(@Validated @RequestBody GetMyReservationsParam param) {
        return meetingService.getMyReservations(param);
    }

    @PostMapping("/getReservation")
    @API(value = "getReservation", remark = "通过会议id查看会议预约信息")
    public CommonResult getReservation(@Validated @RequestBody GetReservationParam param) {
        return meetingService.getReservation(param);
    }

    @PostMapping("/getReservations")
    @API(value = "getReservations", remark = "获取指定月份的会议室预定列表信息")
    public CommonResult getReservations(@Validated @RequestBody GetReservationsParam param){
        return meetingService.getReservations(param);
    }

    @PostMapping("/deleteReservation")
    @API(value = "deleteReservation", remark = "通过id删除会议")
    public CommonResult deleteReservation(@Validated @RequestBody DeleteReservationParam param){
        return meetingService.deleteReservation(param);
    }
}
