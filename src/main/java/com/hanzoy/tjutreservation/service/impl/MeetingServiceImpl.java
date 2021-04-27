package com.hanzoy.tjutreservation.service.impl;

import com.hanzoy.tjutreservation.exception.myExceptions.TimeErrorException;
import com.hanzoy.tjutreservation.mapper.MeetingMapper;
import com.hanzoy.tjutreservation.mapper.UserMapper;
import com.hanzoy.tjutreservation.pojo.bo.UserTokenInfo;
import com.hanzoy.tjutreservation.pojo.dto.CommonResult;
import com.hanzoy.tjutreservation.pojo.dto.param.*;
import com.hanzoy.tjutreservation.pojo.dto.result.GetMyReservationsResult;
import com.hanzoy.tjutreservation.pojo.dto.result.GetReservationResult;
import com.hanzoy.tjutreservation.pojo.dto.result.GetReservationsResult;
import com.hanzoy.tjutreservation.pojo.dto.resultEnum.ResultEnum;
import com.hanzoy.tjutreservation.pojo.po.*;
import com.hanzoy.tjutreservation.service.MeetingService;
import com.hanzoy.tjutreservation.service.SchedulerService;
import com.hanzoy.tjutreservation.service.UserService;
import com.hanzoy.tjutreservation.utils.WechatUtils.WechatUtils;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.Param;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.SendNoticeResult;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.WechatTemplateEnum;
import com.hanzoy.utils.ClassCopyUtils.ClassCopyUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    UserService userService;

    @Resource
    MeetingMapper meetingMapper;

    //未来还将完成耿老师的需求 todo
    @Resource
    UserMapper userMapper;

    @Autowired
    WechatUtils wechatUtils;

    @Autowired
    SchedulerService schedulerService;

    @Override
    @SneakyThrows
    public CommonResult postReservation(PostReservationParam param) {
        //AOP实现token校验

        //获取token中内容
        UserTokenInfo tokenInfo = userService.getUserTokenInfo(param.getToken());

        //查询是否有权限创建会议室
        UserPo user = userMapper.selectUserByOpenid(tokenInfo.getOpenid());
        if(user == null || !user.getCreatAuth()){
            return CommonResult.authError("您没有创建会议室的权利");
        }

        //查询房间id是否存在
        MeetingRoomPo meetingRoomPo = meetingMapper.selectRoomById(new Integer(param.getRoomId()));

        //如果查询不到，则说明填写的资料错误
        if (meetingRoomPo == null) {
            return CommonResult.paramError("会议室id异常");
        }

        //日期拦截
        interceptDate(param.getDate(), param.getStartTime(), param.getEndTime());

        synchronized (MeetingServiceImpl.class) {
            //计算相对00：00时间分钟数的差
            Integer startTime = stringTimeToMinute(param.getStartTime());
            Integer endTime = stringTimeToMinute(param.getEndTime());

            //查询数据库中是否有与将要预定的会议时间上冲突的
            //查询当天所有会议
            ArrayList<MeetingPo> meetings = meetingMapper.selectMeetingByDate(param.getDate(), param.getRoomId());
            //遍历当天会议
            for (MeetingPo meeting : meetings) {

                //计算集合里会议相对00：00时间分钟数的差
                Integer anoStartTime = stringTimeToMinute(meeting.getStartTime());
                Integer anoEndTime = stringTimeToMinute(meeting.getEndTime());

                //检验日期是否冲突，如果开始时间大于会议结束时间则一定不冲突
                if (!(startTime >= anoEndTime || anoStartTime >= endTime)) {
                    return CommonResult.fail(ResultEnum.PARAM_ERROR.getCode(), "日期冲突");
                }
            }
            //整理需要插入的实体类
            MeetingPo meetingPo = new MeetingPo();
            ClassCopyUtils.ClassCopy(meetingPo, param);
            meetingPo.setCreator(tokenInfo.getOpenid());
            //保存数据至数据库

            meetingMapper.insertMeeting(meetingPo);
            meetingMapper.insertParticipant(tokenInfo.getOpenid(), meetingPo.getId(), param.getRemind() == null || param.getRemind());

            //转换开始的时间
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm");
            Date startDate = sdf.parse(param.getDate() + " " + param.getStartTime());

            //开启会议提醒
            schedulerService.startSendRemindTask(String.valueOf(meetingPo.getId()), startDate);
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
        //判断查询结果是否为空
        if(result == null){
            return CommonResult.paramError("会议查询为空");
        }
        //设置是否为创建者
        result.setIsCreator(meetingMapper.selectIsCreator(param.getId(), openid) != null);
        //设置会议状态
        result.setStatus(getTheStatus(result.getDate(), result.getTime()));
        //设置会议是否需要提醒
        Boolean remind = meetingMapper.isRemind(openid, param.getId());
        result.setRemind(remind != null && remind);
        ArrayList<GetReservationResult.User> users = meetingMapper.selectParticipantList(param.getId());

        boolean flag = false;//是否是会议成员
        for (GetReservationResult.User user : users) {
            if (user.getId().equals(openid)) {
                flag = true;
                break;
            }
        }
        if(!flag && !ifAnotherPeopleCanWatchIt){
            return CommonResult.authError();
        }
        result.setParticipant(users);
        //设置用户是否是会议成员
        result.setIsParticipant(flag);
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

    @Override
    public CommonResult deleteReservation(DeleteReservationParam param) {
        //aop实现token校验

        //获取openid
        String openid = userService.getUserTokenInfo(param.getToken()).getOpenid();

        //查询当前用户是否有删除该会议的权限
        if ((meetingMapper.selectIsCreator(param.getId(), openid)==null) && (userMapper.selectIsAdmin(openid) == null)) {
            //当当前用户既不是该会议的创建者也不是管理员时，拒绝该删除请求
            return CommonResult.authError("权限不足，无法删除会议");
        }
        //获取会议详情
        GetReservationResult meetingInfo = meetingMapper.selectMeetingById(new Integer(param.getId()));

        //获取所有需要提醒的用户openid
        ArrayList<String> openidList = meetingMapper.selectParticipantUserByMeetingAndNeedRemind(param.getId());
        for (String theOpenid : openidList) {
            //发送通知
            SendNoticeResult result = wechatUtils.sendNotice(
                    theOpenid,
                    WechatTemplateEnum.CANCEL_MEETING.id,
                    new Param(
                            "thing1", meetingInfo.getName(),
                            "thing3", meetingInfo.getMeetingName(),
                            "date2", meetingInfo.getDate() + " " + meetingInfo.getTime().split("-", 2)[0],
                            "thing4", param.getRemark()
                    ),
                    null,
                    null,
                    null
            );
            if(!result.getErrcode().equals("0")){
                log.warn("==用户提醒发送失败==");
                log.warn("result:      {}",result);
                log.warn("openid:      {}",theOpenid);
                log.warn("meeting:     {}",meetingInfo.getName());
                log.warn("meetingName: {}",meetingInfo.getMeetingName());
                log.warn("time         {}",meetingInfo.getDate()+" "+meetingInfo.getTime());
                log.warn("remark       {}",param.getRemark());
                log.warn("=================");
            }
        }

        //满足条件，执行删除任务
        meetingMapper.deleteMeeting(param.getId());

        return CommonResult.success(null);
    }

    @Override
    public CommonResult remindReservation(RemindReservationParam param) {
        //aop实现token校验

        //获取openid
        String openid = userService.getUserTokenInfo(param.getToken()).getOpenid();

        //发送修改请求
        meetingMapper.updateParticipantRemindByMeetingAndOpenid(String.valueOf(param.getId()), openid, param.getRemind());

        return CommonResult.success(null);
    }

    @Override
    @SneakyThrows
    public CommonResult modifyReservation(ModifyReservationParam param) {

        //AOP实现token校验

        //获取token中内容
        UserTokenInfo tokenInfo = userService.getUserTokenInfo(param.getToken());

        //查询是否有权限修改会议室
        if ((meetingMapper.selectIsCreator(String.valueOf(param.getId()), tokenInfo.getOpenid()) == null && userMapper.selectIsAdmin(tokenInfo.getOpenid()) == null)) {
            return CommonResult.authError("没有权限修改密码");
        }

        //日期拦截
        interceptDate(param.getDate(), param.getStartTime(), param.getEndTime());

        synchronized (MeetingServiceImpl.class) {
            //获取会议详情
            GetReservationResult getReservationResult = meetingMapper.selectMeetingById(Integer.valueOf(param.getId()));

            //查询会议室id
            Integer meetingRoomId = meetingMapper.selectMeetingRoomIdByMeetingId(String.valueOf(param.getId()));

            //计算相对00：00时间分钟数的差
            Integer startTime = stringTimeToMinute(param.getStartTime());
            Integer endTime = stringTimeToMinute(param.getEndTime());

            //查询数据库中是否有与将要预定的会议时间上冲突的
            //查询当天所有会议
            ArrayList<MeetingPo> meetings = meetingMapper.selectMeetingByDate(param.getDate(), String.valueOf(meetingRoomId));
            //遍历当天会议
            for (MeetingPo meeting : meetings) {

                //计算集合里会议相对00：00时间分钟数的差
                Integer anoStartTime = stringTimeToMinute(meeting.getStartTime());
                Integer anoEndTime = stringTimeToMinute(meeting.getEndTime());

                //检验日期是否冲突，如果开始时间大于会议结束时间则一定不冲突
                if (!(startTime >= anoEndTime || anoStartTime >= endTime)) {
                    if(meeting.getId().equals(param.getId())){
                        continue;
                    }
                    return CommonResult.fail(ResultEnum.PARAM_ERROR.getCode(), "日期冲突");
                }
            }
            //整理需要插入的实体类
            MeetingPo meetingPo = new MeetingPo();
            ClassCopyUtils.ClassCopy(meetingPo, getReservationResult);
            ClassCopyUtils.ClassCopy(meetingPo, param);
            meetingPo.setRoomId(String.valueOf(meetingRoomId));
            meetingPo.setCreator(tokenInfo.getOpenid());
            //保存数据至数据库

            meetingMapper.updateMeeting(meetingPo);
            meetingMapper.updateParticipantRemindByMeetingAndOpenid(String.valueOf(param.getId()), tokenInfo.getOpenid(), param.getRemind() == null || param.getRemind());

            //转换开始的时间
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd HH:mm");
            Date startDate = sdf.parse(param.getDate() + " " + param.getStartTime());

            //获取所有需要提醒的用户openid
            ArrayList<String> openidList = meetingMapper.selectParticipantUserByMeetingAndNeedRemind(String.valueOf(param.getId()));
            for (String theOpenid : openidList) {
                //发送通知
                SendNoticeResult result = wechatUtils.sendNotice(
                        theOpenid,
                        WechatTemplateEnum.REVISION_MEETING.id,
                        new Param(
                                "thing1", param.getName(),
                                "thing3", getReservationResult.getName(),
                                "time2", param.getDate() + " " + param.getStartTime()
                        ),
                        null,
                        null,
                        null
                );
                if(!result.getErrcode().equals("0")){
                    log.warn("==用户提醒发送失败==");
                    log.warn("result:      {}",result);
                    log.warn("openid:      {}",theOpenid);
                    log.warn("meeting:     {}",getReservationResult.getName());
                    log.warn("meetingName: {}",getReservationResult.getMeetingName());
                    log.warn("time         {}",param.getDate()+" "+param.getStartTime()+" "+param.getEndTime());
                    log.warn("=================");
                }
            }

            //开启会议提醒
//            schedulerService.starSendRemindTask(String.valueOf(meetingPo.getId()), startDate);
            schedulerService.modifySendRemindTask(String.valueOf(meetingPo.getId()), startDate);
        }
        //正常返回结果
        return CommonResult.success(null);
    }

    @Override
    public CommonResult joinReservation(JoinReservationParam param) {
        //AOP实现token校验

        //获取token中内容
        UserTokenInfo tokenInfo = userService.getUserTokenInfo(param.getToken());

        if(param.getJoin()){
            //查询是否在表中
            ArrayList<String> users = meetingMapper.selectParticipantUserByMeetingAndNeedRemind(String.valueOf(param.getId()));
            boolean flag = false;
            for (String user : users) {
                if(user.equals(tokenInfo.getOpenid())){
                    flag = true;
                    break;
                }
            }

            if(!flag){
                meetingMapper.insertParticipant(tokenInfo.getOpenid(), param.getId(), true);
            }
        }else{
            if (meetingMapper.selectIsCreator(String.valueOf(param.getId()), tokenInfo.getOpenid()) != null) {
                //如果是创建者就不能退出会议
                return CommonResult.serverError("创建者不能退出会议");
            }
            //退出会议
            meetingMapper.deleteParticipantRemindByMeetingAndOpenid(String.valueOf(param.getId()), tokenInfo.getOpenid());
        }
        return CommonResult.success(null);
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

    /**
     * 拦截用户传入的参数的日期
     * @param date 日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    private void interceptDate(String date, String startTime, String endTime){

        //如果开始时间小于等于结束时间，返回日期冲突
        if (stringTimeToMinute(startTime) >= stringTimeToMinute(endTime)) {
            throw new TimeErrorException();
        }

        //拦截时间处于范围外的会议申请
        if (stringTimeToMinute(startTime) < stringTimeToMinute("08:00") || stringTimeToMinute(endTime) > stringTimeToMinute("23:00")){
            throw new TimeErrorException();
        }

        //拦截大于一周以后的会议室申请
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化时间
        sdf.applyPattern("yyyy-MM-dd");
        Date dateValue = null;
        try {
            dateValue = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date today = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(Calendar.DATE,7); //把日期往后增加一天,整数  往后推,负数往前移动
        Date limitDate=calendar.getTime(); //这个时间就是日期往后推一天的结果
        assert dateValue != null;
        if(dateValue.after(limitDate)){
            throw new TimeErrorException();
        }

        //拦截在今天以前的申请
        String[] dateSplit = date.split("-", 3);
        Integer dateInt = new Integer(dateSplit[0] + dateSplit[1] + dateSplit[2]);
        sdf.applyPattern("yyyyMMdd");
        Integer todayValue = new Integer(sdf.format(today));
        if(dateInt < todayValue){
            throw new TimeErrorException();
        }
    }
}
