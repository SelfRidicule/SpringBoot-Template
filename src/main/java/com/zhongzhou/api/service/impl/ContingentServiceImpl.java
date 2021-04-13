package com.zhongzhou.api.service.impl;

import com.zhongzhou.api.entity.Contingent;
import com.zhongzhou.api.entity.Role;
import com.zhongzhou.api.mapper.ContingentMapper;
import com.zhongzhou.api.service.IContingentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongzhou.common.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 队伍 服务实现类
 * </p>
 *
 *
 * @since 2021-03-19
 */
@Service
public class ContingentServiceImpl extends ServiceImpl<ContingentMapper, Contingent> implements IContingentService {

    private static final long serialVersionUID = 5843855239120556468L;

    @Override
    public void initList() {
        List<Contingent> list = list();
        list.forEach(obj -> {
            Constants.MAP_CONTINGENT.put(obj.getId(), obj);
        });
    }
}
