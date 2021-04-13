package com.zhongzhou.api.service;

import com.zhongzhou.api.entity.Activity;
import com.zhongzhou.common.base.BaseService;

/**
 * <p>
 * 活动 服务类
 * </p>
 *
 *
 * @since 2021-03-19
 */
public interface IActivityService extends BaseService<Activity> {

    /**
     * 添加
     *
     * @param activity 活动实体类
     * @return true成功 false失败
     */
    boolean saveActivity(Activity activity);

    /**
     * 修改
     *
     * @param activity 活动实体类
     * @return true成功 false失败
     */
    boolean updateActivity(Activity activity);

    /**
     * 删除
     *
     * @param id 活动id
     * @return true成功 false失败
     */
    boolean removeActivity(Long id);

    /**
     * 查询活动详情
     *
     * @param id 活动id
     * @return Activity
     */
    Activity getDetailActivity(Long id);
}
