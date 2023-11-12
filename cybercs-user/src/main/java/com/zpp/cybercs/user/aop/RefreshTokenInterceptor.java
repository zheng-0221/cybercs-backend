package com.zpp.cybercs.user.aop;

import cn.hutool.core.bean.BeanUtil;
import com.zpp.cybercs.user.model.vo.SafetyUser;
import com.zpp.cybercs.user.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zpp.cybercs.constant.RedisKeys.LOGIN_USER_KEY;
import static com.zpp.cybercs.constant.RedisKeys.LOGIN_USER_TTL;


/**
 * @author zpp
 * @version 1.0
 * @description 执行 token 刷新，将 (token, safetyUser) 保存到 threadLocal 中
 * @date 2023/11/12 22:07:50
 */
@Aspect
@Component
@Slf4j
public class RefreshTokenInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 执行拦截
     */
    @Around("execution(* com.zpp.cybercs.*.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {

        // 1.获取请求头中的 token
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        String token = httpServletRequest.getHeader("authorization");

        if (StringUtils.isNotBlank(token)) {
            // 2.基于TOKEN获取redis中的用户
            String key  = LOGIN_USER_KEY + token;
            Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
            // 3.判断用户是否存在
            if (!userMap.isEmpty()) {
                // 5.将查询到的hash数据转为UserDTO
                SafetyUser safetyUser = BeanUtil.fillBeanWithMap(userMap, new SafetyUser(), false);
                // 6.存在，保存用户信息到 ThreadLocal
                UserHolder.saveLoginUser(safetyUser);
                // 7.刷新token有效期
                stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
            }
        } else {
            log.warn("当前系统未登录");
        }
        // 执行原方法
        return point.proceed();
    }
}
