package com.zhongzhou.api.mapper;

import com.zhongzhou.api.entity.SysUser;
import com.zhongzhou.common.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author
 * @since 2021-03-19
 */
public interface SysUserMapper extends BaseDao<SysUser> {

    List<SysUser> queryActivityUserList(@Param("activityId") Long activityId, @Param("realName") String realName);
}
