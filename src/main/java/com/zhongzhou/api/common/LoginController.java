package com.zhongzhou.api.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhongzhou.api.entity.SysUser;
import com.zhongzhou.api.service.impl.SysUserServiceImpl;
import com.zhongzhou.common.base.BaseController;
import com.zhongzhou.common.bean.ReturnEntity;
import com.zhongzhou.common.bean.ReturnEntityError;
import com.zhongzhou.common.bean.ReturnEntitySuccess;
import com.zhongzhou.common.utils.Constants;
import com.zhongzhou.common.utils.IpUtil;
import com.zhongzhou.common.utils.JwtUtil;
import com.zhongzhou.common.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author
 * @ClassName LoginController
 * @Description 登录与退出
 * @date 2020-06-29 12:19:39
 **/
@RestController
@RequestMapping("/api")
@Slf4j
public class LoginController extends BaseController {
    private static final long serialVersionUID = 7058712925721353762L;

    @Resource
    private SysUserServiceImpl sysUserService;

    /**
     * 用户登录
     *
     * @param userName     用户名
     * @param userPassword 密码
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     * @return ReturnEntity
     */
    @PostMapping("/login")
    public ReturnEntity login(@RequestParam(value = "userName", required = true) String userName, @RequestParam(value = "userPassword", required = true) String userPassword,
                              HttpServletRequest request, HttpServletResponse response) {
        try {
            String ipAddr = IpUtil.getIpAddr(request);
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_name", userName);
            SysUser sysUser = sysUserService.getOne(wrapper);
            if (null != sysUser) {
                if (Md5Util.getSaltverifyMD5(userPassword, sysUser.getUserPassword())) {
                    String authorization = JwtUtil.generateToken(sysUser.getId().toString());
                    tokenController.set(authorization, String.valueOf(sysUser.getId()), Constants.DEFAULT_EXPIRE_SECOND);
                    sysUserService.updateById(sysUser);
                    return new ReturnEntitySuccess(Constants.CODE_SUCCESS, Constants.MSG_LOGIN_SUCCESS, null, authorization);
                } else {
                    return new ReturnEntityError(Constants.CODE_LOGIN_PASSWORD_ERROR, Constants.MSG_LOGIN_PASSWORD_ERROR);
                }
            } else {
                return new ReturnEntityError(Constants.CODE_LOGIN_USERNAME_ERROR, Constants.MSG_LOGIN_USERNAME_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[" + Constants.CODE_LOGIN_ERROR + "]:{}", e.getMessage());
            return new ReturnEntityError(Constants.CODE_LOGIN_ERROR, Constants.MSG_LOGIN_ERROR);
        }
    }

    /**
     * 获取登录信息
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return ReturnEntity
     */
    @GetMapping("/getLoginInfo")
    public ReturnEntity getLoginInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (tokenController.isNotBlank(request, response)) {
                SysUser sysUser = sysUserService.findDetailById(tokenController.getUserId(request, response));
                if (null != sysUser) {
                    return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, sysUser);
                } else {
                    return new ReturnEntityError(1001, Constants.MSG_FIND_NOT_FOUND);
                }
            } else {
                return new ReturnEntityError(1001, Constants.MSG_TOKEN_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnEntityError(1001, Constants.MSG_TOKEN_NOT_FOUND);
        }
    }

    /**
     * 用户退出
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return ReturnEntity
     */
    @PostMapping("/logout")
    public ReturnEntity logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (tokenController.logout(request, response)) {
                return new ReturnEntitySuccess(1001, Constants.MSG_LOGOUT_SUCCESS);
            } else {
                return new ReturnEntityError(Constants.MSG_LOGOUT_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[" + Constants.MSG_LOGOUT_ERROR + "]:{}", e.getMessage());
            return new ReturnEntityError(Constants.MSG_LOGOUT_ERROR);
        }
    }
}
