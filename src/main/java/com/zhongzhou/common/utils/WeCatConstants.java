package com.zhongzhou.common.utils;

import java.io.Serializable;

/**
 * @ClassName WeCatConstants
 * @Description 公众号相关常量
 * @Date 2020/3/12 10:50
 * @Author
 */
public class WeCatConstants implements Serializable {
    private static final long serialVersionUID = 8627153829655249114L;
    /**
     * 公众号token
     */
    public static final String WX_TOKEN = "weixin";
    /**
     * 公众号appId
     */
    public static final String WX_APP_ID = "wx14aa46dd8a1fc3ae";
    /**
     * 公众号AppSecret
     */
    public static final String WX_APP_SECRET = "f72f388a34c2f57b01ce46e903f4a8ce";
    /**
     * 获取access_token的授权类型
     */
    public static final String WX_GRANT_TYPE = "client_credential";
    /**
     * 获取access_token的URL
     */
    public static final String WX_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

}
