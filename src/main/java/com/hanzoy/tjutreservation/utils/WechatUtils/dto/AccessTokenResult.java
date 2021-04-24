package com.hanzoy.tjutreservation.utils.WechatUtils.dto;

import lombok.Data;

@Data
public class AccessTokenResult {
    private String access_token;
    private String expires_in;
}
