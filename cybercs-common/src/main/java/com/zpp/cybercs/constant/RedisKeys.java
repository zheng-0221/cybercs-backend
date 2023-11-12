package com.zpp.cybercs.constant;

/**
 * @author zpp
 * @version 1.0
 * @description redis key 常量
 * @date 2023/11/6 20:03:03
 */
public interface RedisKeys {

    /**
     * 用户登录 token
     */
    String LOGIN_USER_KEY = "login:token:";

    /**
     * 用户登录 token 过期时间
     */
    long LOGIN_USER_TTL = 36000L;
}