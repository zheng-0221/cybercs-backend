package com.zpp.cybercs.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zpp.cybercs.common.BaseResponse;
import com.zpp.cybercs.common.DeleteRequest;
import com.zpp.cybercs.common.ErrorCode;
import com.zpp.cybercs.common.ResultUtils;
import com.zpp.cybercs.exception.ThrowUtils;
import com.zpp.cybercs.user.model.User;
import com.zpp.cybercs.user.model.dto.UserLoginRequest;
import com.zpp.cybercs.user.model.dto.UserQueryRequest;
import com.zpp.cybercs.user.model.dto.UserRegisterRequest;
import com.zpp.cybercs.user.model.dto.UserUpdateRequest;
import com.zpp.cybercs.user.model.vo.SafetyUser;
import com.zpp.cybercs.user.annotation.AdminCheck;
import com.zpp.cybercs.user.service.UserService;
import com.zpp.cybercs.user.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zheng
 * @version 1.0
 * @description 用户管理模块控制器
 * @date 2023/11/4 14:52:12
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<SafetyUser> register(@Validated @RequestBody UserRegisterRequest userRegisterRequest) {

        // 进一步的参数校验
        String password = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        ThrowUtils.throwIf(!password.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次输入密码不同");

        // 执行注册
        SafetyUser safetyUser = userService.register(userRegisterRequest);

        return ResultUtils.success(safetyUser);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<String> login(@Validated @RequestBody UserLoginRequest userLoginRequest) {

        String token = userService.login(userLoginRequest);
        return ResultUtils.success(token);
    }

    /**
     * 注销（退出登录）
     * @return
     */
    @GetMapping("/logout")
    public BaseResponse<Boolean> logout() {

        UserHolder.removeCurrentUser();
        return ResultUtils.success(true);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<SafetyUser> current() {

        SafetyUser currentUser = userService.getCurrentUser();
        return ResultUtils.success(currentUser);
    }

    /**
     * 根据 id 获取脱敏的用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<SafetyUser> getSafetyUserById(long id) {

        SafetyUser safetyUser = userService.getSafetyUserById(id);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 分页获取脱敏用户列表
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<Page<SafetyUser>> listSafetyUser(@Validated @RequestBody UserQueryRequest userQueryRequest) {

        Page<SafetyUser> safetyUserPage = userService.listSafetyUser(userQueryRequest);
        return ResultUtils.success(safetyUserPage);
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/admin/delete")
    @AdminCheck
    public BaseResponse<Boolean> adminDelete(@Validated @RequestBody DeleteRequest deleteRequest) {

        boolean result = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/admin/updateInfo")
    @AdminCheck
    public BaseResponse<Boolean> adminUpdateInfo(@Validated @RequestBody UserUpdateRequest userUpdateRequest) {

        userService.updateInfo(userUpdateRequest);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取未脱敏的用户信息（仅管理员）
     *
     * @param id
     * @return
     */
    @GetMapping("/admin/get")
    @AdminCheck
    public BaseResponse<User> adminGet(long id) {

        User user = userService.getById(id);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(user), ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }


    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/admin/list")
    @AdminCheck
    public BaseResponse<Page<User>> adminList(@Validated @RequestBody UserQueryRequest userQueryRequest) {

        Page<User> userPage = userService.listUser(userQueryRequest);
        return ResultUtils.success(userPage);
    }


}