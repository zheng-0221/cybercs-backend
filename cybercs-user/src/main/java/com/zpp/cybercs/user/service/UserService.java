package com.zpp.cybercs.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zpp.cybercs.user.model.User;
import com.zpp.cybercs.user.model.dto.UserLoginRequest;
import com.zpp.cybercs.user.model.dto.UserQueryRequest;
import com.zpp.cybercs.user.model.dto.UserRegisterRequest;
import com.zpp.cybercs.user.model.dto.UserUpdateRequest;
import com.zpp.cybercs.user.model.vo.SafetyUser;

/**
* @author zheng
* @description 针对表【tb_user(用户表)】的数据库操作Service
* @createDate 2023-11-04 16:13:04
*/
public interface UserService extends IService<User> {

    SafetyUser register(UserRegisterRequest userRegisterRequest);

    String login(UserLoginRequest userLoginRequest);

    SafetyUser getCurrentUser();

    void updateInfo(UserUpdateRequest userUpdateRequest);

    SafetyUser getSafetyUserById(long id);

    Page<SafetyUser> listSafetyUser(UserQueryRequest userQueryRequest);

    Page<User> listUser(UserQueryRequest userQueryRequest);
}
