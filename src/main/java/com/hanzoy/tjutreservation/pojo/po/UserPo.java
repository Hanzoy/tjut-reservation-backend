package com.hanzoy.tjutreservation.pojo.po;

import lombok.Data;

@Data
public class UserPo {
    /**
     * 用户openid
     */
    private String openid;

    /**
     * 用户真实姓名
     */
    private String name;

    /**
     * 用户微信昵称
     */
    private String nickName;

    /**
     * 用户微信头像
     */
    private String avatarUrl;
}
