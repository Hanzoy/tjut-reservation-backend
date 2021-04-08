package com.hanzoy.tjutreservation.mapper;

import com.hanzoy.tjutreservation.pojo.po.MeetingPo;
import com.hanzoy.tjutreservation.pojo.po.MeetingRoomPo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface MeetingMapper {
    /**
     * 向数据库插入会议室信息
     * @param name 会议名称
     * @param openid 创建者id
     * @param roomId 会议室id
     * @param date 日期
     * @param startTime 会议开始时间
     * @param endTime 会议结束时间
     * @param content 会议内容
     */
    void insertMeeting(@Param("name") String name,
                       @Param("openid") String openid,
                       @Param("roomId") String roomId,
                       @Param("date") String date,
                       @Param("startTime") String startTime,
                       @Param("endTime") String endTime,
                       @Param("content") String content);

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
}
