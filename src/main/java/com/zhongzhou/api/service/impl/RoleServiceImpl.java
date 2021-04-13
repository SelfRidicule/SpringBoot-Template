package com.zhongzhou.api.service.impl;

import com.zhongzhou.api.entity.Role;
import com.zhongzhou.api.mapper.RoleMapper;
import com.zhongzhou.api.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongzhou.common.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 *
 * @since 2021-03-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private static final long serialVersionUID = 1596806923504083866L;

    @Override
    public void initList() {
        List<Role> list = list();
        list.forEach(obj -> {
            Constants.MAP_ROLE.put(obj.getId(), obj);
        });
    }
}
