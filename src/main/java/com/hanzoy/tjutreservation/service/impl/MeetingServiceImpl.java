package com.hanzoy.tjutreservation.service.impl;

import com.hanzoy.tjutreservation.mapper.MeetingMapper;
import com.hanzoy.tjutreservation.pojo.bo.UserTokenInfo;
import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.GetMyReservationsParam;
import com.hanzoy.tjutreservation.pojo.dto.param.GetReservationParam;
import com.hanzoy.tjutreservation.pojo.dto.param.GetReservationsParam;
import com.hanzoy.tjutreservation.pojo.dto.param.PostReservationParam;
import com.hanzoy.tjutreservation.pojo.dto.result.GetMyReservationsResult;
import com.hanzoy.tjutreservation.pojo.dto.result.GetReservationResult;
import com.hanzoy.tjutreservation.pojo.dto.result.GetReservationsResult;
import com.hanzoy.tjutreservation.pojo.dto.resultEnum.ResultEnum;
import com.hanzoy.tjutreservation.pojo.po.*;
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
        //拦截时间处于范围外的会议申请
        if (startTime < stringTimeToMinute("08:00") || endTime > stringTimeToMinute("23:00")){
            return CommonResult.fail(ResultEnum.PARAM_ERROR.getCode(), "日期冲突");
        }
        synchronized (MeetingServiceImpl.class) {
            //查询数据库中是否有与将要预定的会议时间上冲突的
            //查询当天所有会议
            ArrayList<MeetingPo> meetings = meetingMapper.selectMeetingByDate(param.getDate(), param.getRoomId());
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
            meeting.setStatus(getTheStatus(meetingPo.getDate(), meetingPo.getStartTime()+"-"+meetingPo.getEndTime()));
            info.add(meeting);
        }
        //将该月会议数据写入result中
        result.setInfo(info);
        return CommonResult.success(result);
    }

    @Override
    public CommonResult getReservation(GetReservationParam param) {
        //是否允许非本会议的人查看会议信息
        boolean ifAnotherPeopleCanWatchIt = true;

        //aop实现token校验

        //创建返回体实体对象
        GetReservationResult result;
        //获取openid
        String openid = userService.getUserTokenInfo(param.getToken()).getOpenid();
        //从数据库中获取会议信息
        result = meetingMapper.selectMeetingById(new Integer(param.getId()));
        //设置是否为创建者
        result.setIsCreator(meetingMapper.selectIsCreator(param.getId(), openid) != null);
        //设置会议状态
        result.setStatus(getTheStatus(result.getDate(), result.getTime()));
        ArrayList<GetReservationResult.User> users = meetingMapper.selectParticipantList(param.getId());

        boolean flag = false;//是否是会议成员
        for (GetReservationResult.User user : users) {
            if(user.getId().equals(openid)){
                flag = true;
            }
        }
        if(!flag && !ifAnotherPeopleCanWatchIt){
            return CommonResult.authError();
        }
        result.setParticipant(users);

        return CommonResult.success(result);
    }

    @Override
    public CommonResult getReservations(GetReservationsParam param) {
        //aop实现token校验

        //创建返回体实体对象
        GetReservationsResult result = new GetReservationsResult();
        //查询数据库中所有的会议室数据
        ArrayList<GetReservationsResult.MeetingRoom> meetingRoom = meetingMapper.selectAllMeetingRoom();
        //将查询的数据写入result中
        result.setMeetingRoom(meetingRoom);
        //数据库查找日历统计信息
        ArrayList<DayPo> dayPos = meetingMapper.selectRiLiByYearAndMonth(param.getYear(), param.getMonth());
        //声明日历数组
        ArrayList<GetReservationsResult.Day> day = new ArrayList<>();
        for (DayPo dayPo : dayPos) {
            GetReservationsResult.Day theDay = new GetReservationsResult.Day();
            //设置属性
            theDay.setDayOfMonth(new Integer(dayPo.getDayOfMonth().split("-", 3)[2]));
            theDay.setCount(dayPo.getCount());

            //查询当前时间下所有会议室情况
            ArrayList<MeetingRoomInfo> meetingRoomInfos = meetingMapper.selectAllRoomInfoByDate(dayPo.getDayOfMonth());
            //声明会议室数组
            ArrayList<GetReservationsResult.Day.MeetingRoomInfo> meetingRoomInfoArrayList = new ArrayList<>();
            for (MeetingRoomInfo meetingRoomInfo : meetingRoomInfos) {
                GetReservationsResult.Day.MeetingRoomInfo theMeetingRoomInfo = new GetReservationsResult.Day.MeetingRoomInfo();
                //设置属性
                theMeetingRoomInfo.setRoomid(meetingRoomInfo.getRoomid());
                theMeetingRoomInfo.setCount(meetingRoomInfo.getCount());

                //查询当前时间，当前会议室下的bar
                ArrayList<BarPo> barPos = meetingMapper.selectBarByDateAndRoomId(dayPo.getDayOfMonth(), meetingRoomInfo.getRoomid());
                ArrayList<GetReservationsResult.Day.MeetingRoomInfo.Bar> barArrayList = new ArrayList<>();

                for (BarPo barPo : barPos) {
                    GetReservationsResult.Day.MeetingRoomInfo.Bar bar = new GetReservationsResult.Day.MeetingRoomInfo.Bar();
                    bar.setStart((stringTimeToMinute(barPo.getStartTime())-stringTimeToMinute("08:00"))/(60.0*15));
                    bar.setEnd((stringTimeToMinute(barPo.getEndTime())-stringTimeToMinute("08:00"))/(60.0*15));
                    barArrayList.add(bar);
                }
                //将bar写入结果中
                theMeetingRoomInfo.setBar(barArrayList);

                //查询当前时间，当前会议室下的meetingInfo
                ArrayList<MeetingInfoPo> meetingPos = meetingMapper.selectMeetingInfo(dayPo.getDayOfMonth(), meetingRoomInfo.getRoomid());
                ArrayList<GetReservationsResult.Day.MeetingRoomInfo.MeetingInfo> meetingInfo = new ArrayList<>();

                for (MeetingInfoPo meetingPo : meetingPos) {
                    GetReservationsResult.Day.MeetingRoomInfo.MeetingInfo anoMeeting = new GetReservationsResult.Day.MeetingRoomInfo.MeetingInfo();
                    ClassCopyUtils.ClassCopy(anoMeeting, meetingPo);
                    meetingInfo.add(anoMeeting);
                }
                //将info写入结果中
                theMeetingRoomInfo.setMeetingInfo(meetingInfo);

                //将单个结果放入集合中
                meetingRoomInfoArrayList.add(theMeetingRoomInfo);
            }
            //将集合放入theDay中
            theDay.setMeetingRoomInfo(meetingRoomInfoArrayList);

            //将单个结果放入集合中
            day.add(theDay);
        }
        //将数据结果集放入result中
        result.setDay(day);
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

    /**
     * 通过当前时间判断状态
     * @param date 日期 yyyy-MM-dd
     * @param time 时间 HH:mm-HH:mm
     * @return "未开始" | "进行中" | "已结束"
     */
    private String getTheStatus(String date, String time){
        //整合状态
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("HH:mm");
        Date nowDateTime = new Date();// 获取当前时间
        Integer nowTime = stringTimeToMinute(sdf.format(nowDateTime));
        sdf.applyPattern("yyyyMMdd");
        Integer nowDate = new Integer(sdf.format(nowDateTime));
        //时间切片
        String[] timeSplit = time.split("-", 2);
        String startTime = timeSplit[0];
        String endTime = timeSplit[1];
        //日期切片
        String[] dateSplit = date.split("-", 3);
        Integer dateInt = new Integer(dateSplit[0] + dateSplit[1] + dateSplit[2]);
        if(nowDate < dateInt){
            return  "未开始";
        }else if(nowDate > dateInt){
            return  "已结束";
        }else {
            if(nowTime < stringTimeToMinute(startTime) ){//如果当前时间小于开始时间则状态为未开始
                return "未开始";
            }else if(nowTime > stringTimeToMinute(endTime)){//如果当前时间大于结束时间则状态为已结束
                return  "已结束";
            }else {//否则将状态设置为进行中
                return  "进行中";
            }
        }
    }
}
