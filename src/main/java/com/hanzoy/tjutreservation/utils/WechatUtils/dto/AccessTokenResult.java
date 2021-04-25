package com.hanzoy.tjutreservation.utils.WechatUtils.dto;

import lombok.Data;

@Data
public class AccessTokenResult {
    /**
     * access_token
     */
    private String access_token;

    /**
     * 有效期
     */
    private String expires_in;
}
