package com.hanzoy.tjutreservation.utils.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanzoy.tjutreservation.utils.WechatUtils.dto.Param;
import lombok.SneakyThrows;
import okhttp3.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();

    public static int FORM = 1;
    public static int JSON = 2;

    /**
     * 封装POST请求
     * @param URL 请求URL
     * @param param 请求参数
     * @param body 请求体
     * @param header 请求头
     * @param resClass 响应体类型
     * @param bodyType 请求体类型
     * @param <T> 泛型
     * @return 响应体
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static  <T> T POST(String URL, Param param, Param body, Param header, Class<T> resClass, int bodyType){
        Map<String, Object> paramMap = null;
        Map<String, Object> headerMap = null;
        //将param转化为map
        if(param != null){
            paramMap = param.toMap();
        }

        //将header转化为map
        if(header != null){
            headerMap = header.toMap();
        }
        StringBuilder stringBuilder = new StringBuilder(URL);
        //将param写入url中
        stringBuilder.append("?");
        if(paramMap != null){
            for(Map.Entry<String, Object> entry : paramMap.entrySet()){
                stringBuilder
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }
        String url = stringBuilder.delete(stringBuilder.length(), stringBuilder.length()).toString();

        RequestBody data = null;
        if(bodyType == FORM){
            Map<String, Object> bodyMap = body.toMap();
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> entry : bodyMap.entrySet()){
                builder.add(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
            }
            data = builder.build();
        }else if(bodyType == JSON){
            String json = objectMapper.writeValueAsString(body.toMap());
            data = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        }

        //将header写入其中
        assert data != null;
        Request.Builder builder = new Request.Builder()
                .post(data)
                .url(url);
        if(header != null){
            for(Map.Entry<String, Object> entry : headerMap.entrySet()){
                builder.addHeader(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
            }
        }

        //生成请求
        Request request = builder.build();
        //发送请求
        String res = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        //请求结果封装到实体类
        return resClass == null ? null : objectMapper.readValue(res, resClass);
    }

    public static  <T> T POST(String URL, Param param, Param body, Class<T> resClass){
        return POST(URL, param, body, null, resClass, JSON);
    }

    public static  <T> T POST(String URL, Param body, Class<T> resClass){
        return POST(URL, null, body, null, resClass, JSON);
    }

    public static Object POST(String URL, Param body){
        return POST(URL, null, body, null, Object.class, JSON);
    }
    /**
     * 封装GET请求
     * @param URL 请求url
     * @param param 请求参数
     * @param header 请求头
     * @param resClass 响应体类型
     * @param <T> 泛型
     * @return 响应体
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static  <T> T GET(String URL, Param param, Param header, Class<T> resClass){
        Map<String, Object> paramMap = null;
        Map<String, Object> headerMap = null;
        //将param转化为map
        if(param != null){
            paramMap = param.toMap();
        }
        //将header转化为map
        if(header != null){
            headerMap = header.toMap();
        }
        StringBuilder stringBuilder = new StringBuilder(URL);
        //将param写入url中
        stringBuilder.append("?");
        if(paramMap != null){
            for(Map.Entry<String, Object> entry : paramMap.entrySet()){
                stringBuilder
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }
        String url = stringBuilder.delete(stringBuilder.length(), stringBuilder.length()).toString();

        //将header写入其中
        Request.Builder builder = new Request.Builder()
                .url(url);
        if(header != null){
            for(Map.Entry<String, Object> entry : headerMap.entrySet()){
                builder.addHeader(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
            }
        }

        //生成请求
        Request request = builder.build();
        //发送请求
        String body = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        //请求结果封装到实体类
        return resClass == null ? null : objectMapper.readValue(body, resClass);
    }

    public static  <T> T GET(String URL, Param param, Class<T> resClass){
        return GET(URL, param, null, resClass);
    }

    public static Object GET(String URL, Param param){
        return GET(URL, param, null, Object.class);
    }

    public static Object GET(String URL){
        return GET(URL, null, null, Object.class);
    }

    @SneakyThrows
    public static Map<String, String> objectToMap(Object o){
        if(o == null){
            return null;
        }
        Class<?> paramClass = o.getClass();
        Field[] fields = paramClass.getDeclaredFields();
        HashMap<String, String> map = new HashMap<>();
        for (Field field : fields) {
            System.out.println(field.getName());
            String name = field.getName();
            if("this$0".equals(name)){
                continue;
            }
            field.setAccessible(true);
            String value = (String)field.get(o);
            map.put(name, value);
        }
        return map;
    }
}
