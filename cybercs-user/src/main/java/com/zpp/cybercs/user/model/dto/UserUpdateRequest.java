package com.zpp.cybercs.user.model.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zpp
 * @version 1.0
 * @description
 * @date 2023/11/9 21:54:56
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1769381782892946043L;

    /**
     * id
     */
    @NotNull
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

}
