package com.hanzoy.tjutreservation.utils.WechatUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.AccessTokenResult;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.AuthorizationResult;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.Param;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.SendNoticeResult;
import com.hanzoy.tjutreservation.utils.request.RequestUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Synchronized;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatUtils {
    /**
     * 小程序 appId
     */
    private String appid;

    /**
     * 小程序 appSecret
     */
    private String secret;

    private static String accessToken;
    private static Date accessTokenTime;

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();

    /**
     * 微信登陆，通过js_code换取session
     *
     * @param js_code 前端登陆的js_code
     * @return 返回登陆结果
     */
    public AuthorizationResult js_codeToSession(String js_code) {

        return RequestUtils.GET(
                "https://api.weixin.qq.com/sns/jscode2session",
                new Param(
                        "appid", appid,
                        "secret", secret,
                        "js_code", js_code,
                        "grant_type", "authorization_code"),
                AuthorizationResult.class);
    }

    /**
     * 获取接口凭证
     * @return accessToken 接口凭证
     */
    @Synchronized
    private String getAccessToken() {
        Date now = new Date(); //取当前时间
        Calendar calendar = new GregorianCalendar();
        //当前时间往前推演1小时
        calendar.add(Calendar.HOUR, -1);
        Date resTime = calendar.getTime();

        if (accessTokenTime == null || resTime.after(accessTokenTime)) {

            accessToken = RequestUtils.GET(
                    "https://api.weixin.qq.com/cgi-bin/token",
                    new Param(
                            "grant_type", "client_credential",
                            "appid", "wx86058a38c5024b53",
                            "secret", "6f2c4ae96115147c2df218364d00f2cd"),
                    AccessTokenResult.class
            ).getAccess_token();
            accessTokenTime = new Date();
        }
        return accessToken;
    }

    public SendNoticeResult sendNotice(String touser, String template_id, Param data, String page, String miniprogram_state, String lang){
        //修改微信脑血栓式data结构
        Map<String, Object> dataMap = data.toMap();
        Map<String, Object> _data = new HashMap<>();
        for(Map.Entry<String, Object> entry: dataMap.entrySet()){
            Map<String, Object> value = new HashMap<>();
            value.put("value", entry.getValue());
            _data.put(entry.getKey(), value);
        }
        return RequestUtils.POST(
                "https://api.weixin.qq.com/cgi-bin/message/subscribe/send",
                new Param("access_token", getAccessToken()),
                new Param(
                        "touser", touser,
                        "template_id", template_id,
                        "page", page,
                        "data", _data,
                        "miniprogram_state", miniprogram_state,
                        "lang", lang
                ).ignoreNull(),
                SendNoticeResult.class
        );
    }
}
