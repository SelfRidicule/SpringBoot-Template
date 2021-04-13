package com.zhongzhou.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhongzhou.api.entity.Role;
import com.zhongzhou.api.service.impl.RoleServiceImpl;
import com.zhongzhou.common.base.BaseController;
import com.zhongzhou.common.base.Pager;
import com.zhongzhou.common.bean.ReturnEntity;
import com.zhongzhou.common.bean.ReturnEntityError;
import com.zhongzhou.common.bean.ReturnEntitySuccess;
import com.zhongzhou.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色
 *
 *
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/api/role")
@Slf4j
public class RoleController extends BaseController {

    private static final long serialVersionUID = -9193465596626426398L;

    @Resource
    private RoleServiceImpl roleService;

    /**
     * 分页查询列表
     *
     * @param current 当前页码
     * @param size    每页数量
     * @param role    Role
     * @return ReturnEntity
     */
    @GetMapping("/page")
    public ReturnEntity selectPageList(Role role, Integer current, Integer size,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            Pager<Role> pager = new Pager<>(current, size);
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("id");
            List<Role> records = roleService.page(pager, wrapper).getRecords();
            int count = roleService.count(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, count, records);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

    /**
     * 查询所有列表
     *
     * @param role Role
     * @return ReturnEntity
     */
    @GetMapping("/list")
    public ReturnEntity selectList(Role role,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("id");
            List<Role> list = roleService.list(wrapper);
            return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, list.size(), list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[" + Constants.MSG_FIND_FAILED + "]:" + e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

    /**
     * 查询详情
     *
     * @param id 主键
     * @return ReturnEntity
     */
    @GetMapping("/detail/{id}")
    public ReturnEntity selectById(@PathVariable("id") Long id) {
        try {
            Role role = roleService.getById(id);
            if (null != role) {
                return new ReturnEntitySuccess(Constants.MSG_FIND_SUCCESS, role);
            } else {
                return new ReturnEntitySuccess(Constants.MSG_FIND_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[id:{} " + Constants.MSG_FIND_FAILED + "]:{}", id, e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

    /**
     * 新增
     *
     * @param role   Role
     * @param result BindingResult
     * @return ReturnEntity
     */
    @PostMapping("/add")
    public ReturnEntity save(@Validated @RequestBody Role role, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldErrors().get(0);
            String errorMsg = fieldError.getDefaultMessage();
            if (Constants.MSG_ERROR_CANNOT_NULL.equals(errorMsg)) {
                errorMsg = fieldError.getField() + fieldError.getDefaultMessage();
            }
            return new ReturnEntityError(errorMsg, null, role);
        } else {
            try {
                role.setCreateTime(LocalDateTime.now());
                role.setCreateUserId(tokenController.getUserId(request, response));
                if (roleService.save(role)) {
                    roleService.initList();
                    return new ReturnEntitySuccess(Constants.MSG_INSERT_SUCCESS, role);
                } else {
                    return new ReturnEntityError(Constants.MSG_INSERT_FAILED, role);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[" + Constants.MSG_INSERT_FAILED + "]:{}", e.getMessage());
                return new ReturnEntityError(e.getMessage());
            }
        }
    }

    /**
     * 修改
     *
     * @param id     主键
     * @param role   Role
     * @param result BindingResult
     * @return ReturnEntity
     */
    @PutMapping("/edit/{id}")
    public ReturnEntity updateById(@PathVariable("id") Long id, @Validated @RequestBody Role role, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return new ReturnEntityError(result.getFieldErrors().get(0).getDefaultMessage(), role);
        } else {
            try {
                if (null == roleService.getById(id)) {
                    return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, role);
                } else {
                    role.setId(id);
                    if (roleService.updateById(role)) {
                        roleService.initList();
                        return new ReturnEntitySuccess(Constants.MSG_UPDATE_SUCCESS, role);
                    } else {
                        return new ReturnEntityError(Constants.MSG_UPDATE_FAILED, role);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[id:{} " + Constants.MSG_UPDATE_FAILED + "]:{}", id, e.getMessage());
                return new ReturnEntityError(e.getMessage());
            }
        }
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return ReturnEntity
     */
    @DeleteMapping("/delete/{id}")
    public ReturnEntity deleteById(@PathVariable("id") Long id) {
        try {
            if (null == roleService.getById(id)) {
                return new ReturnEntityError(Constants.MSG_FIND_NOT_FOUND, id);
            } else {
                if (roleService.removeById(id)) {
                    roleService.initList();
                    return new ReturnEntitySuccess(Constants.MSG_DELETE_SUCCESS, id);
                } else {
                    return new ReturnEntityError(Constants.MSG_DELETE_FAILED, id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[id:{} " + Constants.MSG_DELETE_FAILED + "]:{}", id, e.getMessage());
            return new ReturnEntityError(e.getMessage());
        }
    }

}
