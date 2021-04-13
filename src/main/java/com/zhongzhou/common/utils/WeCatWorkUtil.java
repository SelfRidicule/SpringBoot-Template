package com.zhongzhou.common.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @ClassName WeCatWorkUtil
 * @Description 微信企业号工具类
 * @Date 2021/3/9 15:07
 * @Author
 */
@Component
@Slf4j
public class WeCatWorkUtil implements Serializable {

    private static final long serialVersionUID = -6738748056717216597L;

    @Resource
    private RedisUtil redisUtil;

    private static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
    private static final String JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    //生产环境
//    private static final String CORPID = "wx50039c6fbc9c811f";
//    private static final String AGENTID = "1000034";
//    private static final String SECRET = "eYFNOjHBkyfAXTXUQoNRAfVvN4AjntxU-8kYjB5kjek";
    //公司测试环境
    private static final String CORPID = "ww4558a37848ad3641";
    private static final String AGENTID = "1000065";
    private static final String SECRET = "gxxM7mGOnPJSBxzM0eozFuee_3bgdbjt2w4xWl-T-rI";

    /**
     * 获取access_token
     *
     * @return ReturnEntity
     */
    public String getAccessToken() {
        try {
            String accessToken = redisUtil.get("wx_work_access_token");
            if (StringUtils.isBlank(accessToken)) {
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("corpid", CORPID);
                paramMap.put("corpsecret", SECRET);
                log.info("paramMap json:{}", JSONObject.toJSONString(paramMap));
                String accessTokenUrl = ACCESS_TOKEN_URL + "?corpid=" + CORPID + "&corpsecret=" + SECRET;
                log.info("access token url:{}", accessTokenUrl);
                String result = HttpUtil.get(ACCESS_TOKEN_URL, paramMap);
                JSONObject json = JSONObject.parseObject(result);
                if (null != json) {
                    log.info("result json:{}", json.toJSONString());
                    if (json.containsKey("access_token")) {
                        accessToken = json.getString("access_token");
                        Long expiresTime = json.getLong("expires_in");
                        log.info("accessToken:{}, expiresTime:{}", accessToken, expiresTime);
                        redisUtil.set("wx_work_access_token", accessToken, (expiresTime - 300));
                    } else {
                        throw new RuntimeException("get access_token error!");
                    }
                } else {
                    throw new RuntimeException("url result is null!");
                }
            }
            return accessToken;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException("get access_token error!");
        }
    }

    /**
     * 获取ticket
     *
     * @return ticket
     */
    public String getTicket() {
        try {
            String ticket = redisUtil.get("wx_js_api_ticket");
            if (StringUtils.isBlank(ticket)) {
                String accessToken = getAccessToken();
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("access_token", accessToken);
                paramMap.put("type", "jsapi");
                log.info("paramMap json:{}", JSONObject.toJSONString(paramMap));
                String ticketUrl = JS_API_TICKET_URL + "?access_token=" + accessToken + "&type=jsapi";
                log.info("js_api_ticket url:{}", ticketUrl);
                String result = HttpUtil.get(JS_API_TICKET_URL, paramMap);
                JSONObject json = JSONObject.parseObject(result);
                if (null != json) {
                    log.info("js_api_ticket_json:{}", json.toJSONString());
                    if (json.containsKey("ticket")) {
                        ticket = json.getString("ticket");
                        Long expiresTime = json.getLong("expires_in");
                        log.info("ticket:{}, expiresTime:{}", ticket, expiresTime);
                        redisUtil.set("wx_js_api_ticket", ticket, (expiresTime - 300));
                    } else {
                        log.error("get js_api_ticket error:{}", json.getString("errmsg"));
                        throw new RuntimeException("get js_api_ticket error!");
                    }
                } else {
                    throw new RuntimeException("url result is null!");
                }
            }
            return ticket;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException("get js_api_ticket error!");
        }
    }

    /**
     * 获取企业微信UserId
     *
     * @param code 企业微信授权码
     * @return 企业微信userId
     */
    public String getUserId(String code) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("access_token", getAccessToken());
        paramMap.put("code", code);
        String result = HttpUtil.get("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo", paramMap);
        JSONObject json = JSONObject.parseObject(result);
        if (null != json) {
            log.info("get weCat work json:{}", json.toJSONString());
            if (json.containsKey("errcode") && json.getInteger("errcode") == 0) {
                return json.getString("UserId");
            }
        }
        return null;
    }

}
