package com.zhongzhou.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhongzhou.api.entity.ActivityUser;
import com.zhongzhou.api.entity.SysUser;
import com.zhongzhou.api.service.impl.ActivityUserServiceImpl;
import com.zhongzhou.api.service.impl.SysUserServiceImpl;
import com.zhongzhou.common.base.BaseController;
import com.zhongzhou.common.base.Pager;
import com.zhongzhou.common.bean.ReturnEntity;
import com.zhongzhou.common.bean.ReturnEntityError;
import com.zhongzhou.common.bean.ReturnEntitySuccess;
import com.zhongzhou.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户
 *
 * @author
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/api/sysUser")
@Slf4j
public class SysUserController extends BaseController {

    private static final long serialVersionUID = 6830131377793347530L;

    @Resource
    private SysUserServiceImpl sysUserService;
    @Resource
    private ActivityUserServiceImpl activityUserService;


    /**
     * 分页查询列表
     *
     * @param current 当前页码
     * @param size    每页数量
     * @param sysUser SysUser
     * @return ReturnEntity
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(SysUser sysUser, Integer current, Integer size,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            Pager<SysUser> pager = new Pager<>(current, size);
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(sysUser.getRealName()), "real_name", sysUser.getRealName());
            wrapper.eq(sysUser.getContingentId() != null, "contingent_id", sysUser.getContingentId());
            wrapper.orderByDesc("id");
            List<SysUser> records = sysUserService.page(pager, wrapper).getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                records.forEach(user -> {
                    user = sysUserService.getDetailByUser(user);
                });
            }
            int count = sysUserService.count(wrapper);
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
     * @param sysUser SysUser
     * @return ReturnEntity
     */
    @GetMapping("/list")
    public ReturnEntity selectList(SysUser sysUser,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(sysUser.getRealName()), "real_name", sysUser.getRealName());
            wrapper.eq(sysUser.getContingentId() != null, "contingent_id", sysUser.getContingentId());
            wrapper.orderByDesc("id");
            List<SysUser> list = sysUserService.list(wrapper);
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(user -> {
                    user = sysUserService.getDetailByUser(user);
                });
            }
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, list.size(), list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

    /**
     * 根据队伍查询人员
     *
     * @param sysUser SysUser
     * @return ReturnEntity
     */
    @GetMapping("/queryContingentList")
    public ReturnEntity queryContingentList(SysUser sysUser,
                                            HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.like(StringUtils.isNotBlank(sysUser.getRealName()), "real_name", sysUser.getRealName());
            wrapper.orderByDesc("id");
            List<SysUser> list = sysUserService.list(wrapper);
            if (CollectionUtils.isNotEmpty(list)) {
                list.forEach(user -> {
                    user.setActivityId(sysUser.getActivityId());
                    user = sysUserService.getDetailByUser(user);
                });
                list = list.stream().filter(user -> StringUtils.isNotBlank(user.getContingentName())).collect(Collectors.toList());
            }
            //根据队伍名称进行分组
            Map<String, List<SysUser>> listMap = list.stream().collect(Collectors.groupingBy(SysUser::getContingentName));
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, list.size(), listMap);
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
    @GetMapping("/queryActivityUserList")
    public ReturnEntity queryActivityUserList(Long activityId, String realName,
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
     * 查询详情
     *
     * @param id 主键
     * @return ReturnEntity
     */
    @GetMapping("/detail/{id}")
    public ReturnEntity selectById(@PathVariable("id") Long id) {
        try {
            SysUser sysUser = sysUserService.getById(id);
            if (null != sysUser) {
                sysUser = sysUserService.getDetailByUser(sysUser);
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, sysUser);
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
     * 新增
     *
     * @param sysUser SysUser
     * @param result  BindingResult
     * @return ReturnEntity
     */
    @PostMapping("/add")
    public ReturnEntity save(@Validated @RequestBody SysUser sysUser, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if (Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)) {
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, sysUser);
        } else {
            try {
                sysUser.setCreateTime(LocalDateTime.now());
                sysUser.setCreateUserId(tokenController.getUserId(request, response));
                if (sysUserService.save(sysUser)) {
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, sysUser);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, sysUser);
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
     * @param id      主键
     * @param sysUser SysUser
     * @param result  BindingResult
     * @return ReturnEntity
     */
    @PutMapping("/edit/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody SysUser sysUser, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), sysUser);
        } else {
            try {
                if (null == sysUserService.getById(id)) {
                    return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, sysUser);
                } else {
                    sysUser.setId(id);
                    sysUser.setLastUpdateTime(LocalDateTime.now());
                    sysUser.setLastUpdateUserId(tokenController.getUserId(request, response));
                    if (sysUserService.updateById(sysUser)) {
                        return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, sysUser);
                    } else {
                        return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, sysUser);
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
            if (null == sysUserService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
            } else {
                if (sysUserService.removeById(id)) {
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

    /**
     * 新增活动点位
     *
     * @param activityUser activityUser
     * @param result       BindingResult
     * @return ReturnEntity
     */
    @PostMapping("/addActivityPosition")
    public ReturnEntity addActivityPosition(@Validated @RequestBody ActivityUser activityUser, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if (Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)) {
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, activityUser);
        } else {
            try {
                if (activityUserService.save(activityUser)) {
                    SysUser sysUser = sysUserService.findDetailById(activityUser.getUserId());
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, sysUser);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, activityUser);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[" + Constants.MSG_INSERT_FAILED + "]:{}", e.getMessage());
                return new ReturnEntityError(e.getMessage());
            }
        }
    }

    /**
     * 删除活动点位
     *
     * @return ReturnEntity
     */
    @DeleteMapping("/deleteActivityPosition/{userId}/{activityId}")
    public ReturnEntity deleteActivityPosition(@PathVariable("userId") Long userId, @PathVariable("activityId") Long activityId) {
        try {
            QueryWrapper<ActivityUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            wrapper.eq("activity_id", activityId);
            if (activityUserService.remove(wrapper)) {
                return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, "user_id：" + userId + "  activity_id：" + activityId);
            } else {
                return new ReturnEntityError(Constants.MSG_DELETE_FAILED, "user_id：" + userId + "  activity_id：" + activityId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[id:{} " + Constants.MSG_DELETE_FAILED + "]:{}", "user_id：" + userId + "  activity_id：" + activityId, e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }
}
