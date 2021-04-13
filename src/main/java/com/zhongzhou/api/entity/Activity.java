package com.zhongzhou.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhongzhou.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 活动
 * </p>
 *
 *
 * @since 2021-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_activity")
public class Activity extends BaseEntity {

    private static final long serialVersionUID = 8519989200405582122L;

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 活动标题
     */
    @TableField("title")
    private String title;

    /**
     * 活动内容
     */
    @TableField("content")
    private String content;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 开始时间Str
     */
    @TableField(exist = false)
    private String startTimeStr;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 结束时间Str
     */
    @TableField(exist = false)
    private String endTimeStr;

    /**
     * 状态，0：活动未开始；1：活动进行中；2：活动已结束；
     */
    @TableField("status")
    private Integer status;

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
     * 用户列表
     */
    @TableField(exist = false)
    List<ActivityUser> activityUserList;

}
