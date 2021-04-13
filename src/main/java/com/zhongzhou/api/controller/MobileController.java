package com.zhongzhou.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhongzhou.api.entity.Activity;
import com.zhongzhou.api.entity.SysUser;
import com.zhongzhou.api.service.impl.ActivityServiceImpl;
import com.zhongzhou.api.service.impl.SysUserServiceImpl;
import com.zhongzhou.common.base.BaseController;
import com.zhongzhou.common.base.Pager;
import com.zhongzhou.common.bean.ReturnEntity;
import com.zhongzhou.common.bean.ReturnEntityError;
import com.zhongzhou.common.bean.ReturnEntitySuccess;
import com.zhongzhou.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 活动
 *
 *
 * @since 2021-03-20
 */
@RestController
@RequestMapping("/mobile")
@Slf4j
public class MobileController extends BaseController {

    private static final long serialVersionUID = 3240920541644516288L;

    @Resource
    private ActivityServiceImpl activityService;
    @Resource
    private SysUserServiceImpl sysUserService;


    /**
     * 查询活动详情
     *
     * @param id 主键
     * @return ReturnEntity
     */
    @GetMapping("/activity/detail/{id}")
    public ReturnEntity activityDetail(@PathVariable("id") Long id) {
        try {
            Activity activity = activityService.getDetailActivity(id);
            if (null != activity) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, activity);
            } else {
                return new ReturnEntitySuccess(Constants.MSG_FIND_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[id:{} " + Constants.MSG_FIND_FAILED + "]:{}", id, e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

    /**
     * 活动分页查询列表
     *
     * @param current  当前页码
     * @param size     每页数量
     * @param activity Activity
     * @return ReturnEntity
     */
    @GetMapping("/activity/page")
    public ReturnEntity activitySelectPageList(Activity activity, Integer current, Integer size,
                                               HttpServletRequest request, HttpServletResponse response) {
        try {
            Pager<Activity> pager = new Pager<>(current, size);
            QueryWrapper<Activity> wrapper = new QueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(activity.getTitle()), "title", activity.getTitle());
            wrapper.eq("status", Constants.ACTIVITY_STATUS_STARTED);
            wrapper.orderByDesc("id");
            List<Activity> records = activityService.page(pager, wrapper).getRecords();
            int count = activityService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, records);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

    /**
     * 根据活动id查询对应用户
     *
     * @return ReturnEntity
     */
    @GetMapping("/activity/queryActivityUserList")
    public ReturnEntity activityQueryActivityUserList(Long activityId, String realName,
                                                      HttpServletRequest request, HttpServletResponse response) {
        try {
            List<SysUser> sysUserList = sysUserService.queryActivityUserList(activityId, realName);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, sysUserList.size(), sysUserList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

    /**
     * 查询用户详情
     *
     * @param userId     用户id
     * @param activityId 活动id
     * @return ReturnEntity
     */
    @GetMapping("/sysUser/detail/{userId}/{activityId}")
    public ReturnEntity selectById(@PathVariable("userId") Long userId, @PathVariable("activityId") Long activityId) {
        try {
            SysUser sysUser = sysUserService.getById(userId);
            if (null != sysUser) {
                sysUser.setActivityId(activityId);
                sysUser = sysUserService.getDetailByUser(sysUser);
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, sysUser);
            } else {
                return new ReturnEntitySuccess(Constants.MSG_FIND_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[id:{} " + Constants.MSG_FIND_FAILED + "]:{}", userId, e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

}
