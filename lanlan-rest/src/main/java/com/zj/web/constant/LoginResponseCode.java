package com.zj.web.constant;

/**
 * 返回消息常量
 * @author xiaozj
 */
public class LoginResponseCode {

    public static final String SUCCESS = "2000";

    public static final String SUCCESS_DESC = "处理成功";

    public static final String TOKEN_IS_NULL = "4000";

    public static final String TOKEN_IS_NULL_DESC = "token值为空";

    public static final String TOKEN_EXPIRED = "4001";

    public static final String TOKEN_EXPIRED_DESC = "token值过期";

    public static final String TOKEN_INVALID = "4002";

    public static final String TOKEN_INVALID_DESC = "token无效";

    public static final String USER_ID_IS_NULL = "4100";

    public static final String USER_ID_IS_NULL_DESC = "userId为null";

    public static final String USER_REPEAT = "4101";

    public static final String USER_REPEAT_DESC = "此用户名已注册";

    public static final String USER_REGISTER_FAILED = "4102";

    public static final String USER_REGISTER_FAILED_DESC = "用户注册失败";

    public static final String MISS_INFORMATION = "4103";

    public static final String MISS_INFORMATION_DESC = "缺少注册必须信息";

    public static final String USER_CENTER_ERROR = "4200";

    public static final String USER_CENTER_ERROR_DESC = "用户中心登陆异常";



}
