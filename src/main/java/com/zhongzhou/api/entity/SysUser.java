package com.zhongzhou.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhongzhou.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_user")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = -4959478763251138395L;

    /**
     * 用户ID
     */
    @TableId("id")
    private Long id;

    /**
     * 头像地址
     */
    @TableField("head_img")
    private String headImg;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 密码
     */
    @TableField("user_password")
    private String userPassword;

    /**
     * 用户编码
     */
    @TableField("user_code")
    private String userCode;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 身份证号码
     */
    @TableField("identity_no")
    private String identityNo;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 电话号码
     */
    @TableField("phone")
    private String phone;

    /**
     * 性别，0：男，1：女
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 邮政编码
     */
    @TableField("post_code")
    private String postCode;

    /**
     * 创建者ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 最后一次修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("last_update_time")
    private LocalDateTime lastUpdateTime;

    /**
     * 最后一次修改者ID
     */
    @TableField("last_update_user_id")
    private Long lastUpdateUserId;

    /**
     * 删除标志，0：未删除，1：已删除
     */
    @TableField("delete_flag")
    @TableLogic
    private Integer deleteFlag;

    /**
     * 版本，分布式事务标志
     */
    @TableField("version")
    @Version
    private Long version;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 队伍ID
     */
    @TableField("contingent_id")
    private Long contingentId;

    /**
     * 微信ID
     */
    @TableField("open_id")
    private String openId;


    /**
     * 角色名称
     */
    @TableField(exist = false)
    private String roleName;

    /**
     * 队伍名称
     */
    @TableField(exist = false)
    private String contingentName;

    /**
     * 活动次数
     */
    @TableField(exist = false)
    private Integer activityCount;

    /**
     * 活动状态 : 1存在 0不存在
     */
    @TableField(exist = false)
    private Integer activityFlag;

    /**
     * 活动ID
     */
    @TableField(exist = false)
    private Long activityId;

    /**
     * 经度
     */
    @TableField(exist = false)
    private String longitude;

    /**
     * 纬度
     */
    @TableField(exist = false)
    private String latitude;

    /**
     * 坐标地址
     */
    @TableField(exist = false)
    private String positionAddress;
}
