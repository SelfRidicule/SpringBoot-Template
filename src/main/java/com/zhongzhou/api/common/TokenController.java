package com.zhongzhou.api.common;

import com.zhongzhou.common.utils.Constants;
import com.zhongzhou.common.utils.JwtUtil;
import com.zhongzhou.common.utils.RedisUtil;
import io.github.yedaxia.apidocs.Ignore;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @ClassName TokenController
 * @Description token工具类
 * @Date 2020/6/29 12:39
 * @Author
 */
@Component
@Ignore
public class TokenController implements Serializable {
    private static final long serialVersionUID = 935258820629733612L;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 判断token是否存在
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return true:存在，false:不存在
     */
    public boolean isNotBlank(HttpServletRequest request, HttpServletResponse response) {
        String tokenCode = request.getHeader("Authorization");
        return redisUtil.isNotBlank(tokenCode);
    }

    /**
     * 根据key获取值
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return redisUtil.get(key);
    }

    /**
     * 根据key和value赋值（永久）
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, String value) {
        redisUtil.set(key, value);
    }

    /**
     * 根据key和value赋值（期限）
     *
     * @param key        键
     * @param value      值
     * @param expireTime 到期时间
     */
    public void set(String key, String value, Long expireTime) {
        redisUtil.set(key, value, expireTime);
    }

    /**
     * 获取用户ID
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return 用户ID
     */
    public Long getUserId(HttpServletRequest request, HttpServletResponse response) {
        return JwtUtil.verifyToken(request);
    }

    /**
     * 获取Redis剩余时间
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return 剩余时间
     */
    public Long getExpire(HttpServletRequest request, HttpServletResponse response) {
        String tokenCode = request.getHeader("Authorization");
        if (redisUtil.isNotBlank(tokenCode)) {
            return redisUtil.ttl(tokenCode);
        } else {
            throw new RuntimeException("未找到token");
        }
    }

    /**
     * 更新Redis到期时间
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return true:更新成功；false：更新失败
     */
    public boolean updateExpire(HttpServletRequest request, HttpServletResponse response) {
        String tokenCode = request.getHeader("Authorization");
        if (redisUtil.isNotBlank(tokenCode)) {
            redisUtil.expire(tokenCode, Constants.DEFAULT_EXPIRE_SECOND);
            return true;
        } else {
            throw new RuntimeException("未找到token");
        }
    }

    /**
     * 登出
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return true：退出成功；false：退出失败
     */
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        String tokenCode = request.getHeader("Authorization");
        if (redisUtil.isNotBlank(tokenCode)) {
            redisUtil.del(tokenCode);
        }
        return true;
    }
}
