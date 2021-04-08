package com.hanzoy.tjutreservation.service.impl;

import com.hanzoy.tjutreservation.mapper.MeetingMapper;
import com.hanzoy.tjutreservation.pojo.bo.UserTokenInfo;
import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.PostReservationParam;
import com.hanzoy.tjutreservation.pojo.dto.resultEnum.ResultEnum;
import com.hanzoy.tjutreservation.pojo.po.MeetingPo;
import com.hanzoy.tjutreservation.pojo.po.MeetingRoomPo;
import com.hanzoy.tjutreservation.service.MeetingService;
import com.hanzoy.tjutreservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;


@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    UserService userService;

    @Resource
    MeetingMapper meetingMapper;

    //未来还将完成耿老师的需求 todo

    @Override
    public CommonResult postReservation(PostReservationParam param) {
        //AOP实现token校验

        //获取token中内容
        UserTokenInfo tokenInfo = userService.getUserTokenInfo(param.getToken());

        //查询房间id是否存在
        MeetingRoomPo meetingRoomPo = meetingMapper.selectRoomById(new Integer(param.getRoomId()));

        //如果查询不到，则说明填写的资料错误
        if(meetingRoomPo == null){
            return CommonResult.paramError("会议室id异常");
        }

        //计算相对00：00时间分钟数的差
        Integer startTime = stringTimeToMinute(param.getStartTime());
        Integer endTime = stringTimeToMinute(param.getEndTime());

        //如果开始时间小于等于结束时间，返回日期冲突
        if(startTime >= endTime){
            return CommonResult.fail(ResultEnum.PARAM_ERROR.getCode(), "日期冲突");
        }
        synchronized (MeetingServiceImpl.class){
            //查询数据库中是否有与将要预定的会议时间上冲突的
            //查询当天所有会议
            ArrayList<MeetingPo> meetings = meetingMapper.selectMeetingByDate(param.getDate());
            //遍历当天会议
            for (MeetingPo meeting : meetings) {

                //计算集合里会议相对00：00时间分钟数的差
                Integer anoStartTime = stringTimeToMinute(meeting.getStartTime());
                Integer anoEndTime = stringTimeToMinute(meeting.getEndTime());

                //检验日期是否冲突，如果开始时间大于其他会议结束时间则一定不冲突
                if(!(startTime >= anoEndTime || anoStartTime >= endTime)){
                    return CommonResult.fail(ResultEnum.PARAM_ERROR.getCode(), "日期冲突");
                }
            }
            //保存数据至数据库
            meetingMapper.insertMeeting(param.getName(), tokenInfo.getOpenid(), param.getRoomId(), param.getDate(), param.getStartTime(), param.getEndTime(), param.getContent());
        }
        //正常返回结果
        return CommonResult.success(null);
    }

    /**
     * 传入一个时间字符串，将其转化为与 00:00 时刻的分钟数差
     * @param time 需要转换的时间字符串 格式为 HH:mm
     * @return 返回分钟数差
     */
    private Integer stringTimeToMinute(String time){
        //将传入来的时间进行切片
        String[] timeSplit = time.split(":",2);
        //提取切片后的时间并计算相对00：00时间分钟数的差

        //已拦截转换异常
        return new Integer(timeSplit[0]) * 60 + new Integer(timeSplit[1]);

    }
}
