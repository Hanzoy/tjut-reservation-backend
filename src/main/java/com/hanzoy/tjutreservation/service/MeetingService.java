package com.hanzoy.tjutreservation.service;

import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.GetMyReservationsParam;
import com.hanzoy.tjutreservation.pojo.dto.param.GetReservationParam;
import com.hanzoy.tjutreservation.pojo.dto.param.GetReservationsParam;
import com.hanzoy.tjutreservation.pojo.dto.param.PostReservationParam;

public interface MeetingService {
    /**
     * 用户创建会议
     * @param param 参数
     * @return 返回对象
     */
    CommonResult postReservation(PostReservationParam param);

    /**
     * 获取用户指定月份与自己相关的会议
     * @param param 参数
     * @return 返回对象
     */
    CommonResult getMyReservations(GetMyReservationsParam param);

    /**
     * 通过会议id查看会议预约信息
     * @param param 参数
     * @return 返回对象
     */
    CommonResult getReservation(GetReservationParam param);

    /**
     * 获取所有会议预约信息
     * @param param 参数
     * @return 返回对象
     */
    CommonResult getReservations(GetReservationsParam param);
}
