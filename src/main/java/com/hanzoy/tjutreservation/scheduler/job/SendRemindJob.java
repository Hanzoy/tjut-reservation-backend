package com.hanzoy.tjutreservation.scheduler.job;

import com.hanzoy.tjutreservation.mapper.MeetingMapper;
import com.hanzoy.tjutreservation.pojo.dto.result.GetReservationResult;
import com.hanzoy.tjutreservation.utils.WechatUtils.WechatUtils;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.Param;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.SendNoticeResult;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.WechatTemplateEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

@Slf4j
@Component
public class SendRemindJob implements Job {
    @Autowired
    WechatUtils wechatUtils;

    @Resource
    MeetingMapper meetingMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String meetingId = (String)jobExecutionContext.getJobDetail().getJobDataMap().get("meetingId");
        //获取会议详情
        GetReservationResult meetingInfo = meetingMapper.selectMeetingById(new Integer(meetingId));

        //获取所有需要提醒的用户openid
        ArrayList<String> openidList = meetingMapper.selectParticipantUserByMeetingAndNeedRemind(meetingId);
        for (String theOpenid : openidList) {
            //发送通知
            SendNoticeResult result = wechatUtils.sendNotice(
                    theOpenid,
                    WechatTemplateEnum.REMIND_MEETING.id,
                    new Param(
                            "thing1", meetingInfo.getName(),
                            "thing3", meetingInfo.getMeetingName(),
                            "date2", meetingInfo.getDate() + " " + meetingInfo.getTime().split("-", 2)[0],
                            "thing4", meetingInfo.getContent()
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
                log.warn("remark       {}",meetingInfo.getRemind());
                log.warn("=================");
            }
        }
    }
}
