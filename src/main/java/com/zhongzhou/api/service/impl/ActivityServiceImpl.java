package com.zhongzhou.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongzhou.api.entity.Activity;
import com.zhongzhou.api.entity.ActivityUser;
import com.zhongzhou.api.entity.SysUser;
import com.zhongzhou.api.mapper.ActivityMapper;
import com.zhongzhou.api.service.IActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 活动 服务实现类
 * </p>
 *
 *
 * @since 2021-03-19
 */
@Service
@Slf4j
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    private static final long serialVersionUID = 8790286456541614594L;

    @Resource
    private ActivityUserServiceImpl activityUserService;
    @Resource
    private SysUserServiceImpl sysUserService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean saveActivity(Activity activity) {
        if (save(activity)) {
            //添加活动关联的用户
            addRelationUser(activity);
            return true;
        } else {
            log.error("活动添加失败:{}", activity.toString());
            throw new RuntimeException("活动添加失败");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean updateActivity(Activity activity) {
        if (updateById(activity)) {
            //删除关联的用户信息
            removeRelationUser(activity.getId());
            //添加活动关联的用户
            addRelationUser(activity);
            return true;
        } else {
            log.error("活动修改失败:{}", activity.toString());
            throw new RuntimeException("活动修改失败");
        }
    }

    /**
     * 添加活动关联的用户
     *
     * @param activity 活动实体类
     */
    private void addRelationUser(Activity activity) {
        List<ActivityUser> activityUserList = activity.getActivityUserList();
        if (CollectionUtils.isNotEmpty(activityUserList)) {
            if (!activityUserService.saveBatch(activityUserList)) {
                log.error("活动关联人员添加失败:{}", activity.toString());
                throw new RuntimeException("活动关联人员添加失败");
            }
        }
    }

    /**
     * 删除关联的用户信息
     *
     * @param activityId 活动id
     */
    private void removeRelationUser(Long activityId) {
        if (activityId == null) {
            return;
        }
        QueryWrapper<ActivityUser> wrapper = new QueryWrapper();
        wrapper.eq("activity_id", activityId);
        activityUserService.remove(wrapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public boolean removeActivity(Long id) {
        //删除活动
        if (removeById(id)) {
            //删除关联的用户信息
            removeRelationUser(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Activity getDetailActivity(Long id) {
        Activity activity = getById(id);
        if (activity != null) {
            //查询关联的用户信息
            activity.setActivityUserList(queryRelationUser(id));
        }
        return activity;
    }

    /**
     * 查询关联的用户信息
     *
     * @param activityId 活动id
     * @return List<ActivityUser>
     */
    private List<ActivityUser> queryRelationUser(Long activityId) {
        QueryWrapper<ActivityUser> wrapper = new QueryWrapper();
        wrapper.eq("activity_id", activityId);
        List<ActivityUser> activityUserList = activityUserService.list(wrapper);
        if (CollectionUtils.isNotEmpty(activityUserList)) {
            for (ActivityUser activityUser : activityUserList) {
                if (activityUser.getUserId() != null) {
                    SysUser sysUser = sysUserService.findDetailById(activityUser.getUserId());
                    sysUser.setActivityFlag(1);
                    activityUser.setSysUser(sysUser);
                }
            }
        }
        return activityUserList;
    }

}
