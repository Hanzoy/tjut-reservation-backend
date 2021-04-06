package com.hanzoy.tjutreservation.mapper;

import com.hanzoy.tjutreservation.pojo.po.UserPo;
import org.apache.ibatis.annotations.Param;


public interface UserMapper {
    /**
     * 通过openid查找对应的用户
     * @param openid 用户openid
     * @return 查找的对应用户
     */
    UserPo selectUserByOpenid(@Param("openid") String openid);
}
