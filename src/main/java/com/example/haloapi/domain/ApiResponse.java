package com.example.haloapi.domain;

/**
 * @author Tanzs
 * @date 2025/7/9 下午2:53
 * @description
 */

public class ApiResponse {
    private int code;
    private String message;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ApiResponse() {}

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse of(int code, String message) {
        return new ApiResponse(code, message);
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse(200, "success", data);
    }
}
