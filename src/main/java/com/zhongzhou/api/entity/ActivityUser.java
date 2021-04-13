package com.zhongzhou.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhongzhou.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 活动关联的人
 * </p>
 *
 *
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_activity_user")
public class ActivityUser extends BaseEntity {

    private static final long serialVersionUID = -80654013353843960L;

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @TableField("user_id")
    private Long userId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空")
    @TableField("activity_id")
    private Long activityId;

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    @TableField("longitude")
    private String longitude;

    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    @TableField("latitude")
    private String latitude;

    /**
     * 坐标地址
     */
    @TableField("position_address")
    private String positionAddress;

    /**
     * 用户
     */
    @TableField(exist = false)
    private SysUser sysUser;

}
