package com.zpp.cybercs.user.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 脱敏用户类
 */
@Data
public class SafetyUser implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/vip/admin/banned
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}