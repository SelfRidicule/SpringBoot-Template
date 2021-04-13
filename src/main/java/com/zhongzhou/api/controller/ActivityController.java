package com.zhongzhou.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhongzhou.api.entity.Activity;
import com.zhongzhou.api.service.impl.ActivityServiceImpl;
import com.zhongzhou.common.base.BaseController;
import com.zhongzhou.common.base.Pager;
import com.zhongzhou.common.bean.ReturnEntity;
import com.zhongzhou.common.bean.ReturnEntityError;
import com.zhongzhou.common.bean.ReturnEntitySuccess;
import com.zhongzhou.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 活动
 *
 *
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/api/activity")
@Slf4j
public class ActivityController extends BaseController {

    private static final long serialVersionUID = 2353329468924264245L;

    @Resource
    private ActivityServiceImpl activityService;

    /**
     * 分页查询列表
     *
     * @param current  当前页码
     * @param size     每页数量
     * @param activity Activity
     * @return ReturnEntity
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Activity activity, Integer current, Integer size,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            Pager<Activity> pager = new Pager<>(current, size);
            QueryWrapper<Activity> wrapper = new QueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(activity.getTitle()) , "title" , activity.getTitle());
            wrapper.eq(activity.getStatus() != null, "status", activity.getStatus());
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
     * 查询所有列表
     *
     * @param activity Activity
     * @return ReturnEntity
     */
    @GetMapping("/list")
    public ReturnEntity selectList(Activity activity,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Activity> wrapper = new QueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(activity.getTitle()) , "title" , activity.getTitle());
            wrapper.eq(activity.getStatus() != null, "status", activity.getStatus());
            wrapper.orderByDesc("id");
            List<Activity> list = activityService.list(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, list.size(), list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

    /**
     * 查询详情
     *
     * @param id 主键
     * @return ReturnEntity
     */
    @GetMapping("/detail/{id}")
    public ReturnEntity selectById(@PathVariable("id") Long id) {
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
     * 设置活动进行中
     *
     * @param id 主键
     * @return ReturnEntity
     */
    @PutMapping("/setActivityStarted/{id}")
    public ReturnEntity setActivityStarted(@PathVariable("id") Long id) {
        try {
            if (null == activityService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
            } else {
                UpdateWrapper<Activity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", id);
                updateWrapper.set("status", Constants.ACTIVITY_STATUS_STARTED);
                if (activityService.update(updateWrapper)) {
                    return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, id);
                } else {
                    return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[id:{} " + Constants.MSG_UPDATE_FAILED + "]:{}", id, e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

    /**
     * 新增
     *
     * @param activity Activity
     * @param result   BindingResult
     * @return ReturnEntity
     */
    @PostMapping("/add")
    public ReturnEntity save(@Validated @RequestBody Activity activity, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if (Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)) {
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, activity);
        } else {
            try {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                if (StringUtils.isNotBlank(activity.getStartTimeStr())) {
                    activity.setStartTime(LocalDateTime.parse(activity.getStartTimeStr(), df));
                }
                if (StringUtils.isNotBlank(activity.getEndTimeStr())) {
                    activity.setEndTime(LocalDateTime.parse(activity.getEndTimeStr(), df));
                }
                activity.setCreateTime(LocalDateTime.now());
                activity.setCreateUserId(tokenController.getUserId(request, response));
                if (activityService.save(activity)) {
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, activity);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, activity);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[" + Constants.MSG_INSERT_FAILED + "]:{}", e.getMessage());
                return new ReturnEntityError(e.getMessage());
            }
        }
    }

    /**
     * 修改
     *
     * @param id       主键
     * @param activity Activity
     * @param result   BindingResult
     * @return ReturnEntity
     */
    @PutMapping("/edit/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody Activity activity, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), activity);
        } else {
            try {
                if (null == activityService.getById(id)) {
                    return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, activity);
                } else {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    if (StringUtils.isNotBlank(activity.getStartTimeStr())) {
                        activity.setStartTime(LocalDateTime.parse(activity.getStartTimeStr(), df));
                    }
                    if (StringUtils.isNotBlank(activity.getEndTimeStr())) {
                        activity.setEndTime(LocalDateTime.parse(activity.getEndTimeStr(), df));
                    }
                    activity.setId(id);
                    activity.setLastUpdateTime(LocalDateTime.now());
                    activity.setLastUpdateUserId(tokenController.getUserId(request, response));
                    if (activityService.updateById(activity)) {
                        return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, activity);
                    } else {
                        return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, activity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[id:{} " + Constants.MSG_UPDATE_FAILED + "]:{}", id, e.getMessage());
                return new ReturnEntityError(e.getMessage());
            }
        }
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return ReturnEntity
     */
    @DeleteMapping("/delete/{id}")
    public ReturnEntity deleteById(@PathVariable("id") Long id) {
        try {
            if (null == activityService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
            } else {
                if (activityService.removeActivity(id)) {
                    return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
                } else {
                    return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[id:{} " + Constants.MSG_DELETE_FAILED + "]:{}", id, e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

}
