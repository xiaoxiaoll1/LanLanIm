package com.zj.exception;

/**
 * @author xiaozj
 */
public enum ExceptionEnum {

    /**
     * redis中记录不存在
     */
    REDIS_NOT_FOUND("1000","redis中记录不存在"),

    BAD_REQUEST("4000", "请求失效，请重新登录!")

    ;

    private String code;
    private String msg;

    ExceptionEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
