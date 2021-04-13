package com.zhongzhou.api.service;

import com.zhongzhou.api.entity.Contingent;
import com.zhongzhou.common.base.BaseService;

/**
 * <p>
 * 队伍 服务类
 * </p>
 *
 *
 * @since 2021-03-19
 */
public interface IContingentService extends BaseService<Contingent> {

    /**
     * 初始化
     */
    void initList();
}
