package com.zpp.cybercs.user;


import com.zpp.cybercs.user.model.dto.UserLoginRequest;
import com.zpp.cybercs.user.model.dto.UserRegisterRequest;
import com.zpp.cybercs.user.model.vo.SafetyUser;
import com.zpp.cybercs.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class CybercsUserApplicationTests {

    @Resource
    private UserService userService;

    @Test
    void registerTest() {

        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUserAccount("202108060711");
        userRegisterRequest.setUserPassword("123456");
        userRegisterRequest.setCheckPassword("123456");
        userRegisterRequest.setUsername("zpp");
        userRegisterRequest.setUserProfile("施哲 王铁成 刘子安的父亲");

        SafetyUser safetyUser = userService.register(userRegisterRequest);
        Assertions.assertNotNull(safetyUser);
    }

    @Test
    void loginTest() {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUserAccount("202108060711");
        userLoginRequest.setUserPassword("123456");

        String token = userService.login(userLoginRequest);
        log.info("{} token : {}", userLoginRequest.getUserAccount(), token);
    }
}
