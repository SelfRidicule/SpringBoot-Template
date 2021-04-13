package com.zhongzhou.api.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhongzhou.api.entity.Activity;
import com.zhongzhou.api.service.impl.ActivityServiceImpl;
import com.zhongzhou.common.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName ActivityTask
 * @Description 活动定时任务
 * @Date 2021/3/20 8:49
 * @Author
 */
@EnableScheduling
@Component
@Slf4j
public class ActivityTask extends BaseController {

    @Resource
    private ActivityServiceImpl activityService;

    /**
     * 修改已过期的活动状态为已结束
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void changeStatusForEndActivity() {
        try {
            QueryWrapper<Activity> wrapper = new QueryWrapper<>();
            wrapper.ne("status", 2);
            wrapper.ge("end_time", LocalDateTime.now());
            List<Activity> list = activityService.list(wrapper);
            if (null != list && list.size() > 0) {
                for (Activity activity : list) {
                    activity.setStatus(2);
                }
                activityService.updateBatchById(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("修改活动状态定时任务执行失败:{}", e.getMessage());
        }
    }
}
