package com.zhongzhou.common.utils;

import com.zhongzhou.api.entity.Contingent;
import com.zhongzhou.api.entity.Role;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Constants
 * @Description 魔法值常量
 * @Date 2020/3/8 17:11
 * @Author
 */
public class Constants implements Serializable {
    private static final long serialVersionUID = -7153088581645065553L;
    /**
     * Content-Type：application/json; charset=utf-8
     */
    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    /**
     * 默认日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    /**
     * AES加密解密的password
     */
    public static final String AES_PASSWORD = "zz@jszzkj.cn";
    /**
     * 默认系统登录有效期
     */
    public static final long DEFAULT_EXPIRE_SECOND = 1800;
    /**
     * 成功code
     */
    public static final Integer CODE_SUCCESS = 0;
    /**
     * 成功msg
     */
    public static final String MSG_SUCCESS = "操作成功";
    /**
     * 错误code
     */
    public static final Integer CODE_ERROR = 1;
    /**
     * 错误msg
     */
    public static final String MSG_ERROR = "操作失败";

    /**
     * 未知异常code
     */
    public static final Integer CODE_UNKNOWN_ERROR_MSG = -1;
    /**
     * 未知异常msg
     */
    public static final String MSG_UNKNOWN_ERROR_MSG = "未知异常";
    public static final String MSG_ERROR_CANNOT_NULL = "不能为null";
    public static final String MSG_CONNECTION_ERROR = "连接异常";
    public static final String MSG_FIND_NOT_FOUND = "未查询到结果";
    public static final String MSG_FIND_EXISTED = "数据已存在";
    public static final String MSG_FIND_SUCCESS = "查询成功";
    public static final String MSG_FIND_FAILED = "查询失败";
    public static final String MSG_INSERT_SUCCESS = "新增成功";
    public static final String MSG_INSERT_FAILED = "新增失败";
    public static final String MSG_UPDATE_SUCCESS = "修改成功";
    public static final String MSG_UPDATE_FAILED = "修改失败";
    public static final String MSG_DELETE_SUCCESS = "删除成功";
    public static final String MSG_DELETE_FAILED = "删除失败";
    public static final String MSG_UPLOAD_SUCCESS = "上传成功";
    public static final String MSG_UPLOAD_FAILED = "上传失败";
    public static final String MSG_DOWNLOAD_SUCCESS = "下载成功";
    public static final String MSG_DOWNLOAD_FAILED = "下载失败";

    public static final Integer CODE_TOKEN_NOT_FOUND = 1001;
    public static final String MSG_TOKEN_NOT_FOUND = "未登录或已过期";
    public static final String MSG_LOGIN_SUCCESS = "登录成功";
    public static final String MSG_LOGOUT_SUCCESS = "登出成功";
    public static final Integer CODE_LOGIN_ERROR = 1201;
    public static final String MSG_LOGIN_ERROR = "登录失败";
    public static final String MSG_LOGOUT_ERROR = "登出失败";
    public static final Integer CODE_LOGIN_USERNAME_ERROR = 1202;
    public static final String MSG_LOGIN_USERNAME_ERROR = "用户名不存在";
    public static final Integer CODE_LOGIN_PASSWORD_ERROR = 1203;
    public static final String MSG_LOGIN_PASSWORD_ERROR = "用户名或密码错误";
    public static final String MSG_LOGIN_LOCKED_ERROR = "用户已被冻结";
    public static final String MSG_LOGIN_UNAUTH_ERROR = "未登录";
    public static final Integer CODE_TOPIC_ID_NOT_FOUND = 1401;
    public static final String MSG_TOPIC_ID_NOT_FOUND = "header中缺少topicID参数";
    public static final Integer CODE_TOPIC_WEBSITE_NOT_FOUND = 1404;
    public static final String MSG_TOPIC_WEBSITE_NOT_FOUND = "主题子网站不存在";


    public static final String MSG_GRANT_SUCCESS = "授权成功";
    public static final String MSG_GRANT_FAILED = "授权失败";
    public static final Integer CODE_FORBIDDEN = 403;
    public static final String MSG_FORBIDDEN = "权限不足，请联系管理员";
    public static final Integer CODE_LOCKED = 0;

    public static final Long NUM_ZERO = 0L;
    public static final Long NUM_TEN = 10L;

    public static final String ERROR_IDENTITY_VALIDATE = "身份证号码非法";
    public static final String ERROR_PHONE_VALIDATE = "电话号码非法";

    public static final String MSG_GRAND_POLICE_LIKE_SUCCESS = "点赞成功";
    public static final String MSG_GRAND_POLICE_LIKE_FAILED = "点赞失败";

    public static final Integer CODE_IP_VALIDATE = 2001;
    public static final String MSG_IP_VALIDATE = "IP不在允许范围内";

    /**
     * 角色集合
     */
    public static Map<Long, Role> MAP_ROLE = new HashMap<>();
    /**
     * 队伍集合
     */
    public static Map<Long, Contingent> MAP_CONTINGENT = new HashMap<>();

    /**
     * 启用
     */
    public static final Integer STATUS_ENABLE = 1;
    /**
     * 禁用
     */
    public static final Integer STATUS_DISABLE = 0;
    /**
     * 是
     */
    public static final Integer FLAG_YES = 1;
    /**
     * 否
     */
    public static final Integer FLAG_NO = 0;
    /**
     * 活动-状态-0：活动未开始
     */
    public static final Integer ACTIVITY_STATUS_NOT_START = 0;
    /**
     * 活动-状态-1：活动进行中；
     */
    public static final Integer ACTIVITY_STATUS_STARTED = 1;
    /**
     * 活动-状态-2：活动已结束；
     */
    public static final Integer ACTIVITY_STATUS_END = 2;
}
