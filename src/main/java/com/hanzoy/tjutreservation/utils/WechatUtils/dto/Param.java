package com.hanzoy.tjutreservation.utils.WechatUtils.dto;

import com.hanzoy.utils.ClassCopyUtils.ClassCopyUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Param {
    private String key1_name;
    private Object key1_value;
    private String key2_name;
    private Object key2_value;
    private String key3_name;
    private Object key3_value;
    private String key4_name;
    private Object key4_value;
    private String key5_name;
    private Object key5_value;
    private String key6_name;
    private Object key6_value;
    private String key7_name;
    private Object key7_value;
    private String key8_name;
    private Object key8_value;
    private String key9_name;
    private Object key9_value;
    private String key10_name;
    private Object key10_value;
    private String key11_name;
    private Object key11_value;
    private String key12_name;
    private Object key12_value;
    private String key13_name;
    private Object key13_value;
    private String key14_name;
    private Object key14_value;
    private String key15_name;
    private Object key15_value;

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value, String key7_name, Object key7_value, String key8_name, Object key8_value, String key9_name, Object key9_value, String key10_name, Object key10_value, String key11_name, Object key11_value, String key12_name, Object key12_value, String key13_name, Object key13_value, String key14_name, Object key14_value, String key15_name, Object key15_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
        this.key7_name = key7_name;
        this.key7_value = key7_value;
        this.key8_name = key8_name;
        this.key8_value = key8_value;
        this.key9_name = key9_name;
        this.key9_value = key9_value;
        this.key10_name = key10_name;
        this.key10_value = key10_value;
        this.key11_name = key11_name;
        this.key11_value = key11_value;
        this.key12_name = key12_name;
        this.key12_value = key12_value;
        this.key13_name = key13_name;
        this.key13_value = key13_value;
        this.key14_name = key14_name;
        this.key14_value = key14_value;
        this.key15_name = key15_name;
        this.key15_value = key15_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value, String key7_name, Object key7_value, String key8_name, Object key8_value, String key9_name, Object key9_value, String key10_name, Object key10_value, String key11_name, Object key11_value, String key12_name, Object key12_value, String key13_name, Object key13_value, String key14_name, Object key14_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
        this.key7_name = key7_name;
        this.key7_value = key7_value;
        this.key8_name = key8_name;
        this.key8_value = key8_value;
        this.key9_name = key9_name;
        this.key9_value = key9_value;
        this.key10_name = key10_name;
        this.key10_value = key10_value;
        this.key11_name = key11_name;
        this.key11_value = key11_value;
        this.key12_name = key12_name;
        this.key12_value = key12_value;
        this.key13_name = key13_name;
        this.key13_value = key13_value;
        this.key14_name = key14_name;
        this.key14_value = key14_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value, String key7_name, Object key7_value, String key8_name, Object key8_value, String key9_name, Object key9_value, String key10_name, Object key10_value, String key11_name, Object key11_value, String key12_name, Object key12_value, String key13_name, Object key13_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
        this.key7_name = key7_name;
        this.key7_value = key7_value;
        this.key8_name = key8_name;
        this.key8_value = key8_value;
        this.key9_name = key9_name;
        this.key9_value = key9_value;
        this.key10_name = key10_name;
        this.key10_value = key10_value;
        this.key11_name = key11_name;
        this.key11_value = key11_value;
        this.key12_name = key12_name;
        this.key12_value = key12_value;
        this.key13_name = key13_name;
        this.key13_value = key13_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value, String key7_name, Object key7_value, String key8_name, Object key8_value, String key9_name, Object key9_value, String key10_name, Object key10_value, String key11_name, Object key11_value, String key12_name, Object key12_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
        this.key7_name = key7_name;
        this.key7_value = key7_value;
        this.key8_name = key8_name;
        this.key8_value = key8_value;
        this.key9_name = key9_name;
        this.key9_value = key9_value;
        this.key10_name = key10_name;
        this.key10_value = key10_value;
        this.key11_name = key11_name;
        this.key11_value = key11_value;
        this.key12_name = key12_name;
        this.key12_value = key12_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value, String key7_name, Object key7_value, String key8_name, Object key8_value, String key9_name, Object key9_value, String key10_name, Object key10_value, String key11_name, Object key11_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
        this.key7_name = key7_name;
        this.key7_value = key7_value;
        this.key8_name = key8_name;
        this.key8_value = key8_value;
        this.key9_name = key9_name;
        this.key9_value = key9_value;
        this.key10_name = key10_name;
        this.key10_value = key10_value;
        this.key11_name = key11_name;
        this.key11_value = key11_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value, String key7_name, Object key7_value, String key8_name, Object key8_value, String key9_name, Object key9_value, String key10_name, Object key10_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
        this.key7_name = key7_name;
        this.key7_value = key7_value;
        this.key8_name = key8_name;
        this.key8_value = key8_value;
        this.key9_name = key9_name;
        this.key9_value = key9_value;
        this.key10_name = key10_name;
        this.key10_value = key10_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value, String key7_name, Object key7_value, String key8_name, Object key8_value, String key9_name, Object key9_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
        this.key7_name = key7_name;
        this.key7_value = key7_value;
        this.key8_name = key8_name;
        this.key8_value = key8_value;
        this.key9_name = key9_name;
        this.key9_value = key9_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value, String key7_name, Object key7_value, String key8_name, Object key8_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
        this.key7_name = key7_name;
        this.key7_value = key7_value;
        this.key8_name = key8_name;
        this.key8_value = key8_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value, String key7_name, Object key7_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
        this.key7_name = key7_name;
        this.key7_value = key7_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value, String key6_name, Object key6_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
        this.key6_name = key6_name;
        this.key6_value = key6_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value, String key5_name, Object key5_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
        this.key5_name = key5_name;
        this.key5_value = key5_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value, String key4_name, Object key4_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
        this.key4_name = key4_name;
        this.key4_value = key4_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value, String key3_name, Object key3_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
        this.key3_name = key3_name;
        this.key3_value = key3_value;
    }

    public Param(String key1_name, Object key1_value, String key2_name, Object key2_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
        this.key2_name = key2_name;
        this.key2_value = key2_value;
    }

    public Param(String key1_name, Object key1_value) {
        this.key1_name = key1_name;
        this.key1_value = key1_value;
    }

    public Param() {
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        if(key1_name != null){
            if(key1_value instanceof Param){
                map.put(key1_name, ((Param)key1_value).toMap());
            }else{
                map.put(key1_name, key1_value);
            }
        }
        if(key2_name != null){
            if(key2_value instanceof Param){
                map.put(key2_name, ((Param)key2_value).toMap());
            }else{
                map.put(key2_name, key2_value);
            }
        }
        if(key3_name != null){
            if(key3_value instanceof Param){
                map.put(key3_name, ((Param)key3_value).toMap());
            }else{
                map.put(key3_name, key3_value);
            }
        }
        if(key4_name != null){
            if(key4_value instanceof Param){
                map.put(key4_name, ((Param)key4_value).toMap());
            }else{
                map.put(key4_name, key4_value);
            }
        }
        if(key5_name != null){
            if(key5_value instanceof Param){
                map.put(key5_name, ((Param)key5_value).toMap());
            }else{
                map.put(key5_name, key5_value);
            }
        }
        if(key6_name != null){
            if(key6_value instanceof Param){
                map.put(key6_name, ((Param)key6_value).toMap());
            }else{
                map.put(key6_name, key6_value);
            }
        }
        if(key7_name != null){
            if(key7_value instanceof Param){
                map.put(key7_name, ((Param)key7_value).toMap());
            }else{
                map.put(key7_name, key7_value);
            }
        }
        if(key8_name != null){
            if(key8_value instanceof Param){
                map.put(key8_name, ((Param)key8_value).toMap());
            }else{
                map.put(key8_name, key8_value);
            }
        }
        if(key9_name != null){
            if(key9_value instanceof Param){
                map.put(key9_name, ((Param)key9_value).toMap());
            }else{
                map.put(key9_name, key9_value);
            }
        }
        if(key10_name != null){
            if(key10_value instanceof Param){
                map.put(key10_name, ((Param)key10_value).toMap());
            }else{
                map.put(key10_name, key10_value);
            }
        }
        if(key11_name != null){
            if(key11_value instanceof Param){
                map.put(key11_name, ((Param)key11_value).toMap());
            }else{
                map.put(key11_name, key11_value);
            }
        }
        if(key12_name != null){
            if(key12_value instanceof Param){
                map.put(key12_name, ((Param)key12_value).toMap());
            }else{
                map.put(key12_name, key12_value);
            }
        }
        if(key13_name != null){
            if(key13_value instanceof Param){
                map.put(key13_name, ((Param)key13_value).toMap());
            }else{
                map.put(key13_name, key13_value);
            }
        }
        if(key14_name != null){
            if(key14_value instanceof Param){
                map.put(key14_name, ((Param)key14_value).toMap());
            }else{
                map.put(key14_name, key14_value);
            }
        }
        if(key15_name != null){
            if(key15_value instanceof Param){
                map.put(key15_name, ((Param)key15_value).toMap());
            }else{
                map.put(key15_name, key15_value);
            }
        }
        return map;
    }

    public Param ignoreNull(){
        Param param = new Param();
        ClassCopyUtils.ClassCopy(param, this);
        if(param.key1_value == null){
            param.key1_name = null;
        }
        if(param.key2_value == null){
            param.key2_name = null;
        }
        if(param.key3_value == null){
            param.key3_name = null;
        }
        if(param.key4_value == null){
            param.key4_name = null;
        }
        if(param.key5_value == null){
            param.key5_name = null;
        }
        if(param.key6_value == null){
            param.key6_name = null;
        }
        if(param.key7_value == null){
            param.key7_name = null;
        }
        if(param.key8_value == null){
            param.key8_name = null;
        }
        if(param.key9_value == null){
            param.key9_name = null;
        }
        if(param.key10_value == null){
            param.key10_name = null;
        }
        if(param.key11_value == null){
            param.key11_name = null;
        }
        if(param.key12_value == null){
            param.key12_name = null;
        }
        if(param.key13_value == null){
            param.key13_name = null;
        }
        if(param.key14_value == null){
            param.key14_name = null;
        }
        if(param.key15_value == null){
            param.key15_name = null;
        }
        return param;
    }
}
