package com.hanzoy.tjutreservation.service.impl;

import com.hanzoy.tjutreservation.mapper.MeetingMapper;
import com.hanzoy.tjutreservation.pojo.bo.UserTokenInfo;
import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.GetMyReservationsParam;
import com.hanzoy.tjutreservation.pojo.dto.param.PostReservationParam;
import com.hanzoy.tjutreservation.pojo.dto.result.GetMyReservationsResult;
import com.hanzoy.tjutreservation.pojo.dto.resultEnum.ResultEnum;
import com.hanzoy.tjutreservation.pojo.po.MeetingPo;
import com.hanzoy.tjutreservation.pojo.po.MeetingRoomPo;
import com.hanzoy.tjutreservation.service.MeetingService;
import com.hanzoy.tjutreservation.service.UserService;
import com.hanzoy.utils.ClassCopyUtils.ClassCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
        if (meetingRoomPo == null) {
            return CommonResult.paramError("会议室id异常");
        }

        //计算相对00：00时间分钟数的差
        Integer startTime = stringTimeToMinute(param.getStartTime());
        Integer endTime = stringTimeToMinute(param.getEndTime());

        //如果开始时间小于等于结束时间，返回日期冲突
        if (startTime >= endTime) {
            return CommonResult.fail(ResultEnum.PARAM_ERROR.getCode(), "日期冲突");
        }
        synchronized (MeetingServiceImpl.class) {
            //查询数据库中是否有与将要预定的会议时间上冲突的
            //查询当天所有会议
            ArrayList<MeetingPo> meetings = meetingMapper.selectMeetingByDate(param.getDate());
            //遍历当天会议
            for (MeetingPo meeting : meetings) {

                //计算集合里会议相对00：00时间分钟数的差
                Integer anoStartTime = stringTimeToMinute(meeting.getStartTime());
                Integer anoEndTime = stringTimeToMinute(meeting.getEndTime());

                //检验日期是否冲突，如果开始时间大于其他会议结束时间则一定不冲突
                if (!(startTime >= anoEndTime || anoStartTime >= endTime)) {
                    return CommonResult.fail(ResultEnum.PARAM_ERROR.getCode(), "日期冲突");
                }
            }
            //整理需要插入的实体类
            MeetingPo meetingPo = new MeetingPo();
            ClassCopyUtils.ClassCopy(meetingPo, param);
            meetingPo.setCreator(tokenInfo.getOpenid());
            //保存数据至数据库

            Integer integer = meetingMapper.insertMeeting(meetingPo);
            meetingMapper.insertParticipant(tokenInfo.getOpenid(), meetingPo.getId());
        }
        //正常返回结果
        return CommonResult.success(null);
    }

    @Override
    public CommonResult getMyReservations(GetMyReservationsParam param) {
        //aop实现token校验

        //创建返回体实体对象
        GetMyReservationsResult result = new GetMyReservationsResult();
        //获取openid
        String openid = userService.getUserTokenInfo(param.getToken()).getOpenid();
        //遍历数据库找到每月对应的表
        ArrayList<Integer> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Integer count = meetingMapper.selectCountForYearByMonth(param.getYear(), i + 1 + "", openid);
            months.add(count);
        }
        //将每年月份总计量写入result中
        result.setMonths(months);

        ArrayList<GetMyReservationsResult.Meeting> info = new ArrayList<>();


        //查询集合
        ArrayList<MeetingPo> meetingPos = meetingMapper.selectMeetingByYearAndMonth(param.getYear(), param.getMonth(), openid);
        for (MeetingPo meetingPo : meetingPos) {
            GetMyReservationsResult.Meeting meeting = new GetMyReservationsResult.Meeting();
            //类拷贝
            ClassCopyUtils.ClassCopy(meeting, meetingPo);
            //时间整合
            meeting.setTime(meetingPo.getStartTime() + "-" + meetingPo.getEndTime());
            //整合状态
            //获取当前时间
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("HH:mm");
            Date nowDateTime = new Date();// 获取当前时间
            Integer nowTime = stringTimeToMinute(sdf.format(nowDateTime));
            sdf.applyPattern("yyyyMMdd");
            Integer nowDate = new Integer(sdf.format(nowDateTime));

            String[] dateSplit = meetingPo.getDate().split("-", 3);
            Integer date = new Integer(dateSplit[0] + dateSplit[1] + dateSplit[2]);
            if(nowDate < date){
                meeting.setStatus("未开始");
            }else if(nowDate > date){
                meeting.setStatus("已结束");
            }else {
                if(nowTime < stringTimeToMinute(meetingPo.getStartTime()) ){//如果当前时间小于开始时间则状态为未开始
                    meeting.setStatus("未开始");
                }else if(nowTime > stringTimeToMinute(meetingPo.getEndTime())){//如果当前时间大于结束时间则状态为已结束
                    meeting.setStatus("已结束");
                }else {//否则将状态设置为进行中
                    meeting.setStatus("进行中");
                }
            }
            info.add(meeting);
        }
        //将该月会议数据写入result中
        result.setInfo(info);
        return CommonResult.success(result);
    }

    /**
     * 传入一个时间字符串，将其转化为与 00:00 时刻的分钟数差
     *
     * @param time 需要转换的时间字符串 格式为 HH:mm
     * @return 返回分钟数差
     */
    private Integer stringTimeToMinute(String time) {
        //将传入来的时间进行切片
        String[] timeSplit = time.split(":", 2);
        //提取切片后的时间并计算相对00：00时间分钟数的差

        //已拦截转换异常
        return new Integer(timeSplit[0]) * 60 + new Integer(timeSplit[1]);

    }
}
