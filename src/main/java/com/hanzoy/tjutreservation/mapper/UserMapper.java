package com.hanzoy.tjutreservation.mapper;

import com.hanzoy.tjutreservation.pojo.po.UserPo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;


public interface UserMapper {
    /**
     * 通过openid查找对应的用户
     * @param openid 用户openid
     * @return 查找的对应用户
     */
    UserPo selectUserByOpenid(@Param("openid") String openid);

    /**
     * 通过名字查找对应的用户
     * @param name 用户名字
     * @return 查找的对应用户集合
     */
    ArrayList<UserPo> selectUserByName(@Param("name") String name);

    /**
     * 通过openid修改user，包括修改openid
     * @param openid 需要修改的openid
     * @param newOpenid 修改后的openid
     * @param name 修改后的name
     * @param nickName 修改后的微信昵称
     * @param avatarUrl 修改后的微信头像
     * @param creatAuth 修改后的创建权限
     */
    void updateUserByOpneidWithOpenid(@Param("openid") String openid,
                                      @Param("newOpenid") String newOpenid,
                                      @Param("name") String name,
                                      @Param("nickName") String nickName,
                                      @Param("avatarUrl") String avatarUrl,
                                      @Param("creatAuth") Boolean creatAuth);

    /**
     * 插入user
     * @param openid 待插入用户openid，唯一标识
     * @param name 待插入待用户姓名
     * @param nickName 待插入待用户微信昵称
     * @param avatarUrl 待插入待用户微信头像
     * @param creatAuth 是否有创建会议室的权利
     */
    void insertUser(@Param("openid") String openid,
                    @Param("name") String name,
                    @Param("nickName") String nickName,
                    @Param("avatarUrl") String avatarUrl,
                    @Param("creatAuth") Boolean creatAuth);

    /**
     * 查询是否为管理员
     * @param openid 待查询用户openid
     * @return 返回结果是否为null
     */
    Integer selectIsAdmin(@Param("openid") String openid);
}
