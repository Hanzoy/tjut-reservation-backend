package com.hanzoy.tjutreservation.utils.WechatUtils.dto;

public enum WechatTemplateEnum {
    CANCEL_MEETING("zoNiVDerNHmRIxEqslSyanW9RftA6jjS66E2w8EVYaM"),
    REVISION_MEETING("Sx99rGlCxNt580Zjpu1sNleOoloXV4M2L55YLScM-xI"),
    REMIND_MEETING("0JMcKVMwcxTR30FccNilNA5ETndnWXUheHG4t_lFcRY");

    public final String id;
    WechatTemplateEnum(String id) {
        this.id = id;
    }
}
