package com.zhongzhou.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.zhongzhou.api.common.TokenController;
import com.zhongzhou.common.bean.ReturnEntityError;
import com.zhongzhou.common.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName PermissionInterceptor
 * @Description 权限菜单拦截器
 * @Date 2020/3/8 17:46
 * @Author
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {
    /**
     * token即将到期时间
     */
    private final static Long EXPIRETIME = 300L;

    @Resource
    private TokenController tokenController;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("被拦截啦！！！！！！！！！！");
        String uri = request.getRequestURI();
        System.out.println("uri=" + uri);
        String authorization = request.getHeader("Authorization");
        System.out.println("authorization=" + authorization);
        String method = request.getMethod();
        System.out.println("method=" + method);

        boolean authFlag = false;
        if (StringUtils.isNotBlank(authorization)) {
            try {
                if (tokenController.isNotBlank(request, response)) {
                    if (tokenController.getExpire(request, response) < EXPIRETIME) {
                        tokenController.updateExpire(request, response);
                    }
                    Long userId = tokenController.getUserId(request, response);
                    //判断是否为超级管理员登录
                    if (null != userId && userId == 1L) {
                        String url = uri.replace("/security", "");
                        System.out.println("url=" + url);
                        authFlag = true;
                    } else {
                        PrintWriter writer = response.getWriter();
                        //设置编码为UTF-8
                        response.setCharacterEncoding("utf-8");
                        response.setContentType(Constants.CONTENT_TYPE_JSON);
                        //1202--用户名不存在
                        writer.append(JSONObject.toJSONString(new ReturnEntityError(Constants.CODE_LOGIN_USERNAME_ERROR, Constants.MSG_LOGIN_USERNAME_ERROR)));
                    }
                } else {
                    PrintWriter writer = response.getWriter();
                    //设置编码为UTF-8
                    response.setCharacterEncoding("utf-8");
                    response.setContentType(Constants.CONTENT_TYPE_JSON);
                    //1001--未登录或已过期
                    writer.append(JSONObject.toJSONString(new ReturnEntityError(Constants.CODE_TOKEN_NOT_FOUND, Constants.MSG_TOKEN_NOT_FOUND)));
                }
            } catch (IOException e) {
                e.printStackTrace();
                PrintWriter writer = response.getWriter();
                //设置编码为UTF-8
                response.setCharacterEncoding("utf-8");
                response.setContentType(Constants.CONTENT_TYPE_JSON);
                //1001--未登录或已过期
                writer.append(JSONObject.toJSONString(new ReturnEntityError(Constants.CODE_TOKEN_NOT_FOUND, Constants.MSG_TOKEN_NOT_FOUND)));
            }
        } else {
            PrintWriter writer = response.getWriter();
            //设置编码为UTF-8
            response.setCharacterEncoding("utf-8");
            response.setContentType(Constants.CONTENT_TYPE_JSON);
            //1001--未登录或已过期
            writer.append(JSONObject.toJSONString(new ReturnEntityError(Constants.CODE_TOKEN_NOT_FOUND, Constants.MSG_TOKEN_NOT_FOUND)));
            authFlag = false;
        }
        return authFlag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
