package com.zhongzhou.common.config;

import com.zhongzhou.api.service.impl.ContingentServiceImpl;
import com.zhongzhou.api.service.impl.RoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @ClassName InitConfig
 * @Description 系统初始化
 * @Date 2020/6/28 13:59
 * @Author
 */
@Component
@Slf4j
public class InitConfig implements ApplicationRunner {

    @Resource
    private ContingentServiceImpl contingentService;
    @Resource
    private RoleServiceImpl roleService;


    @PostConstruct
    public void construct() {
        log.info("系统启动中............");
    }

    @PreDestroy
    public void dostory() {
        log.info("系统关闭中............");
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("**************初始化角色**************");
        roleService.initList();
        log.info("**************初始化队伍**************");
        contingentService.initList();
        log.info("**************系统初始化完成**************");
    }
}
