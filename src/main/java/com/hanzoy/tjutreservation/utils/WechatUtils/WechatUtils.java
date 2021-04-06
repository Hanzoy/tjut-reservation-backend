package com.hanzoy.tjutreservation.utils.WechatUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.AuthorizationResult;
import lombok.Data;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Data
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

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();
    public AuthorizationResult js_codeToSession(String js_code){
        try {
            //后端登陆url
            String URL = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type=authorization_code";
            //封装请求
            Request request = new Request.Builder()
                    .url(URL)
                    .build();
            //创建会话
            Call call = client.newCall(request);
            //发送请求
            String body = Objects.requireNonNull(call.execute().body()).string();
            //请求结果封装到实体类
            return objectMapper.readValue(body, AuthorizationResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //其他错误返回
        return new AuthorizationResult(null, null, null, "-2", "http请求错误");
    }
}
