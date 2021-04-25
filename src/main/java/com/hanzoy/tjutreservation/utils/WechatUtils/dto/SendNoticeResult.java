package com.hanzoy.tjutreservation.utils.WechatUtils.dto;

import lombok.Data;

@Data
public class SendNoticeResult {

    /**
     * 返回码
     */
    private String errcode;

    /**
     * 返回信息
     */
    private String errmsg;

    /**
     * 会话id
     */
    private String rid;
    private String msgid;
}
