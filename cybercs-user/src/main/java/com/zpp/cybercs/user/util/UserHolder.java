package com.zpp.cybercs.user.util;

import cn.hutool.core.bean.BeanUtil;
import com.zpp.cybercs.user.model.User;
import com.zpp.cybercs.user.model.vo.SafetyUser;

/**
 * @author zpp
 * @version 1.0
 * @description User ThreadLocal 操作工具类
 * @date 2023/11/5 15:45:26
 */
public class UserHolder {

    private static final ThreadLocal<SafetyUser> tl = new ThreadLocal<>();

    public static void saveLoginUser(SafetyUser safetyUser){
        tl.set(safetyUser);
    }

    public static SafetyUser getCurrentUser(){
        return tl.get();
    }

    public static void removeCurrentUser(){
        tl.remove();
    }

    public static SafetyUser toSafety(User user) {
        SafetyUser safetyUser = new SafetyUser();
        BeanUtil.copyProperties(user, safetyUser);
        return safetyUser;
    }
}
