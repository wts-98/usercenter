package com.wts.usercenter.common;

/**
 * 错误码
 *
 * @author wts
 */
public enum ErrorCode {

    SUCCESS(0,"OK",""),
    PARANS_ERROR(40000,"请求参数错误",""),
    PARANS_NULL(40001,"请求数据为空",""),
    NO_LOGIN(40101,"无登录状态",""),
    NO_AUTH(40102,"无权限",""),
    SYSTEM_ERROR(50000,"系统内部异常","")
    ;
    private int code;
    /**
     * 状态码信息
     */
    private String message;
    /**
     * 状态信息描述
     */
    private String description;

    ErrorCode() {
    }

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
