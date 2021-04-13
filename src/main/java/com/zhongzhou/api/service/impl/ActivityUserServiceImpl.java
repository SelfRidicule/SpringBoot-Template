package com.zhongzhou.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongzhou.api.entity.ActivityUser;
import com.zhongzhou.api.mapper.ActivityUserMapper;
import com.zhongzhou.api.service.IActivityUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活动关联的人 服务实现类
 * </p>
 *
 *
 * @since 2021-03-19
 */
@Service
public class ActivityUserServiceImpl extends ServiceImpl<ActivityUserMapper, ActivityUser> implements IActivityUserService {

    private static final long serialVersionUID = 5685611188838537523L;

    @Override
    public Integer queryUserCount(Long userId) {
        if (userId == null) {
            return 0;
        }
        QueryWrapper<ActivityUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return count(wrapper);
    }

}
