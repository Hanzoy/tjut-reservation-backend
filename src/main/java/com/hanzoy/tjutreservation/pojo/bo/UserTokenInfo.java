package com.hanzoy.tjutreservation.pojo.bo;

import lombok.Data;

/**
 * token内容实体类
 */
@Data
public class UserTokenInfo {
    /**
     * 用户openid
     */
    private String openid;

    /**
     * 用户姓名
     */
    private String name;
}
