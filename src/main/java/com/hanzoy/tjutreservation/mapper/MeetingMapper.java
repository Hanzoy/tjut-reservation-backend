package com.hanzoy.tjutreservation.mapper;

import com.hanzoy.tjutreservation.pojo.dto.result.GetReservationResult;
import com.hanzoy.tjutreservation.pojo.dto.result.GetReservationsResult;
import com.hanzoy.tjutreservation.pojo.po.*;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface MeetingMapper {
    /**
     * 向数据库插入会议信息
     * @param meeting 待插入的会议数据
     */
    void insertMeeting(MeetingPo meeting);

    /**
     * 向数据库修改会议信息
     * @param meeting 待修改的会议数据
     */
    void updateMeeting(MeetingPo meeting);

    /**
     * 通过date来搜索会议
     * @param date 需要搜索的日期
     * @param roomid 会议室id
     * @return 查询的结果
     */
    ArrayList<MeetingPo> selectMeetingByDate(@Param("date") String date, @Param("roomid") String roomid);

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
    void insertParticipant(@Param("openid") String openid, @Param("meeting") Integer meeting, @Param("remind") Boolean remind);

    /**
     * 查询每个月的总预约次数
     * @param year 查询的年份
     * @param month 查询的月份
     * @param openid 用户openid
     * @return 返回当月的申请总量
     */
    Integer selectCountForYearByMonth(@Param("year") String year, @Param("month") String month, @Param("openid") String openid);

    /**
     * 查询每年每个月用户的使用数据
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

    /**
     * 搜索某一会议的参加者人数修改
     * @param meetingId 会议id
     * @return 用户结果集
     */
    ArrayList<GetReservationResult.User> selectParticipantList(@Param("id") String meetingId);

    /**
     * 查询所有会议室集合
     * @return 查询结果
     */
    ArrayList<GetReservationsResult.MeetingRoom> selectAllMeetingRoom();

    /**
     * 通过year和month查看某一月的日历情况
     * @param year 年
     * @param month 月
     * @return 返回结果
     */
    ArrayList<DayPo> selectRiLiByYearAndMonth(@Param("year") String year, @Param("month") String month);

    /**
     * 通过日期查找当前所有房间的信息
     * @param date 日期
     * @return 查询结果
     */
    ArrayList<MeetingRoomInfo> selectAllRoomInfoByDate(@Param("date") String date);

    /**
     * 通过日期和会议室id查找时间轴
     * @param date 日期
     * @param roomid 会议室id
     * @return 查询结果
     */
    ArrayList<BarPo> selectBarByDateAndRoomId(@Param("date") String date, @Param("roomid") Integer roomid);

    /**
     * 查询当前时间，会议室下的meetingInfo
     * @param date 日期
     * @param roomid 会议室id
     * @return 查询结果
     */
    ArrayList<MeetingInfoPo> selectMeetingInfo(@Param("date") String date, @Param("roomid") Integer roomid);

    /**
     * 查询是否需要提醒
     * @param openid 用户openid
     * @param meetingId 会议id
     * @return 查询结果
     */
    Boolean isRemind(@Param("openid") String openid, @Param("meetingId") String meetingId);

    /**
     * 通过会议id删除会议
     * @param meetingId 会议的id
     */
    void deleteMeeting(@Param("meetingId") String meetingId);

    /**
     * 通过会议ID查询需要通知的用户
     * @param meetingId 会议id
     * @return 用户openid
     */
    ArrayList<String> selectParticipantUserByMeetingAndNeedRemind(@Param("meetingId") String meetingId);

    /**
     * 通过openid和meetingId修改是否需要提醒
     * @param meetingId 会议id
     * @param openid 用户openid
     * @param remind 是否提醒
     */
    void updateParticipantRemindByMeetingAndOpenid(@Param("meetingId")String meetingId, @Param("openid")String openid, @Param("remind")Boolean remind);

    /**
     * 通过openid和meetingId退出会议
     * @param meetingId 会议id
     * @param openid 用户openid
     */
    void deleteParticipantRemindByMeetingAndOpenid(@Param("meetingId")String meetingId, @Param("openid")String openid);
    /**
     * 通过会议ia查询会议室id
     * @param meetingId 会议id
     * @return 会议室id
     */
    Integer selectMeetingRoomIdByMeetingId(@Param("meetingId") String meetingId);
}
