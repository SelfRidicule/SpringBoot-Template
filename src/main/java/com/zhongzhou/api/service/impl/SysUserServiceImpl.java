package com.zhongzhou.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongzhou.api.entity.ActivityUser;
import com.zhongzhou.api.entity.Contingent;
import com.zhongzhou.api.entity.Role;
import com.zhongzhou.api.entity.SysUser;
import com.zhongzhou.api.mapper.SysUserMapper;
import com.zhongzhou.api.service.ISysUserService;
import com.zhongzhou.common.utils.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author
 * @since 2021-03-19
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private static final long serialVersionUID = -7986079097823666949L;

    @Resource
    private ContingentServiceImpl contingentService;
    @Resource
    private RoleServiceImpl roleService;
    @Resource
    private ActivityUserServiceImpl activityUserService;
    @Resource
    private SysUserMapper sysUserMapper;


    @Override
    public SysUser findDetailById(Long userId) {
        SysUser user = getById(userId);
        return getDetailByUser(user);
    }

    @Override
    public SysUser getDetailByUser(SysUser user) {
        if (user != null) {
            //角色
            if (user.getRoleId() != null) {
                Role role = Constants.MAP_ROLE.get(user.getRoleId());
                if (role != null) {
                    user.setRoleName(role.getName());
                }
            }
            //队伍
            if (user.getContingentId() != null) {
                Contingent contingent = Constants.MAP_CONTINGENT.get(user.getContingentId());
                if (contingent != null) {
                    user.setContingentName(contingent.getName());
                }
            }
            //活动次数
            user.setActivityCount(activityUserService.queryUserCount(user.getId()));
            //活动是否存在
            user.setActivityFlag(userActivityExist(user));
            //赋值用户关联活动位置信息
            userSetActivityPosition(user);
        }
        return user;
    }

    /**
     * 赋值用户关联活动位置信息
     *
     * @param user 用户实体类
     */
    private void userSetActivityPosition(SysUser user) {
        if (user != null && user.getId() != null && user.getActivityId() != null) {
            QueryWrapper<ActivityUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", user.getId());
            wrapper.eq("activity_id", user.getActivityId());
            List<ActivityUser> activityUserList = activityUserService.list(wrapper);
            if (CollectionUtils.isNotEmpty(activityUserList)) {
                ActivityUser activityUser = activityUserList.get(0);
                user.setLongitude(activityUser.getLongitude());
                user.setLatitude(activityUser.getLatitude());
                user.setPositionAddress(activityUser.getPositionAddress());
            }
        }
    }

    /**
     * 用户在活动是否存在
     *
     * @param user 用户实体类
     * @return 活动状态 : 1存在 0不存在
     */
    private int userActivityExist(SysUser user) {
        if (user != null && user.getId() != null && user.getActivityId() != null) {
            QueryWrapper<ActivityUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", user.getId());
            wrapper.eq("activity_id", user.getActivityId());
            int count = activityUserService.count(wrapper);
            if (count > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public List<SysUser> queryActivityUserList(Long activityId, String realName) {
        return sysUserMapper.queryActivityUserList(activityId, realName);
    }

}
