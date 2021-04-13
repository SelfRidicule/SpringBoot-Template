package com.zhongzhou.api.service;

import com.zhongzhou.api.entity.ActivityUser;
import com.zhongzhou.common.base.BaseService;

/**
 * <p>
 * 活动关联的人 服务类
 * </p>
 *
 *
 * @since 2021-03-19
 */
public interface IActivityUserService extends BaseService<ActivityUser> {

    /**
     * 查询用户活动次数
     *
     * @param userId 用户id
     * @return 总数
     */
    Integer queryUserCount(Long userId);
}
