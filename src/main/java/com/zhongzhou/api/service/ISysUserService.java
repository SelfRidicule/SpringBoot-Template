package com.zhongzhou.api.service;

import com.zhongzhou.api.entity.SysUser;
import com.zhongzhou.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author
 * @since 2021-03-19
 */
public interface ISysUserService extends BaseService<SysUser> {

    /**
     * 详情
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    SysUser findDetailById(Long userId);

    /**
     * 获取用户关联信息
     *
     * @param sysUser 用户
     * @return 用户信息
     */
    SysUser getDetailByUser(SysUser sysUser);

    /**
     * 根据活动id查询对应用户
     *
     * @param activityId 活动id
     * @param realName   用户名称
     * @return List<SysUser>
     */
    List<SysUser> queryActivityUserList(Long activityId, String realName);
}
