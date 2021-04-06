package com.hanzoy.tjutreservation.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hanzoy.tjutreservation.pojo.dto.resultEnum.ResultEnum;
import lombok.Data;

/**
 * 统一返回前端类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResult {
    private String code;
    private String message;
    private Object data;

    public CommonResult(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResult(ResultEnum resultEnum, Object data){
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }

    public static CommonResult success(String code, String message, Object data) {
        return new CommonResult(code, message, data);
    }

    public static CommonResult success(String code, String message) {
        return success(code, message, null);
    }

    public static CommonResult success(Object data) {
        return new CommonResult(ResultEnum.SUCCESS, data);
    }

    public static CommonResult fail(String code, String message, Object data) {
        return new CommonResult(code, message, data);
    }

    public static CommonResult fail(String code, String message) {
        return fail(code, message, null);
    }

    public static CommonResult paramError(String message){
        return new CommonResult(ResultEnum.PARAM_ERROR.getCode(), message);
    }

    public static CommonResult paramError(){
        return new CommonResult(ResultEnum.PARAM_ERROR, null);
    }
}