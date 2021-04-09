package com.hanzoy.tjutreservation.mapper;

import com.hanzoy.tjutreservation.pojo.dto.result.GetMyReservationsResult;
import com.hanzoy.tjutreservation.pojo.dto.result.GetReservationResult;
import com.hanzoy.tjutreservation.pojo.po.MeetingPo;
import com.hanzoy.tjutreservation.pojo.po.MeetingRoomPo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface MeetingMapper {
    /**
     * 向数据库插入会议信息
     * @param meeting 带插入的会议数据
     */
    Integer insertMeeting(MeetingPo meeting);

    /**
     * 通过date来搜索会议
     * @param date 需要搜索的日期
     * @return 查询的结果
     */
    ArrayList<MeetingPo> selectMeetingByDate(@Param("date") String date);

    /**
     * 通过会议室id查询对应的会议室
     * @param id 带查询的会议室id
     * @return 查询结果
     */
    MeetingRoomPo selectRoomById(@Param("id") Integer id);

    /**
     * 向数据库中插入参加会议接口
     * @param openid 用户openid
     * @param meeting 会议id
     */
    void insertParticipant(@Param("openid") String openid, @Param("meeting") Integer meeting);

    /**
     * 查询每个月的总犯规次数
     * @param year 查询的年份
     * @param month 查询的月份
     * @param openid 用户openid
     * @return 返回当月的申请总量
     */
    Integer selectCountForYearByMonth(@Param("year") String year, @Param("month") String month, @Param("openid") String openid);

    /**
     * 查询每年每个月用户的使用样本
     * @param year 年
     * @param month 月
     * @param openid 用户openid
     * @return 查询结果
     */
    ArrayList<MeetingPo> selectMeetingByYearAndMonth(@Param("year") String year, @Param("month") String month, @Param("openid") String openid);

    /**
     * 通过id查找对应的会议
     * @param id 会议id
     * @return 查询结果
     */
    GetReservationResult selectMeetingById(@Param("id") Integer id);

    /**
     * 查找是否为某会议的创建者
     * @param meetingId 会议id
     * @param openid 用户openid
     * @return 返回结果是否为null
     */
    Integer selectIsCreator(@Param("id") String meetingId, @Param("openid") String openid);

    ArrayList<GetReservationResult.User> selectParticipantList(@Param("id") String meetingId);
}
