package com.zpp.cybercs.user.aop;

import com.zpp.cybercs.user.annotation.AdminCheck;
import com.zpp.cybercs.common.ErrorCode;
import com.zpp.cybercs.user.model.vo.SafetyUser;
import com.zpp.cybercs.user.util.UserHolder;
import com.zpp.cybercs.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


import static com.zpp.cybercs.user.model.UserConstant.ADMIN_ROLE;

/**
 * 权限校验 AOP
 */
@Aspect
@Component
@Slf4j
public class AuthInterceptor {

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param adminCheck
     * @return
     */
    @Around("@annotation(adminCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AdminCheck adminCheck) throws Throwable {

        // 当前登录用户
        SafetyUser currentUser = UserHolder.getCurrentUser();
        String userRole = currentUser.getUserRole();

        // 校验管理员身份
        boolean notAdmin = !StringUtils.equals(userRole, ADMIN_ROLE);
        ThrowUtils.throwIf(notAdmin, ErrorCode.NO_AUTH_ERROR);

        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

