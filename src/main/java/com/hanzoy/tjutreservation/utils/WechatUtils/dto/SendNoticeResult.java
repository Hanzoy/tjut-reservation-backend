package com.hanzoy.tjutreservation.utils.WechatUtils.dto;

import lombok.Data;

@Data
public class SendNoticeResult {
    private String errcode;
    private String errmsg;
    private String rid;
    private String msgid;
}
