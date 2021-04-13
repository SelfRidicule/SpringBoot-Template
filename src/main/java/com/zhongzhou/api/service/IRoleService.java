package com.zhongzhou.api.service;

import com.zhongzhou.api.entity.Role;
import com.zhongzhou.common.base.BaseService;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 *
 * @since 2021-03-19
 */
public interface IRoleService extends BaseService<Role> {

    /**
     * 初始化
     */
    void initList();
}
