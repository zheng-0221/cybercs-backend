package com.zpp.cybercs.user.model.dto;


import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author zpp
 * @version 1.0
 * @description 用户注册请求封装类
 * @date 2023/11/4 18:31:22
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -8366797946564354311L;
    /**
     * 账号 4到16位（字母，数字，下划线，减号）
     */
    @NotBlank(message = "账号不能为空")
//    @Pattern(message = "用户名为4到16位（字母，数字，下划线，减号）", regexp = "/^[a-zA-Z0-9_-]{4,16}$/")
    @Length(message = "账号为 8 到 11 位", min = 8, max = 11)
    private String userAccount;

    /**
     * 密码 由6-22位数字和英文混合组成
     */
    @NotBlank(message = "密码不能为空")
//    @Pattern(message = "由6-22位数字和英文混合组成", regexp = "/^[a-zA-Z0-9]{6,22}$/")
    @Length(message = "密码最短为 6 位，最长为 22 位", min = 6, max = 22)
    private String userPassword;

    /**
     * 再次输入密码
     */
    @NotBlank(message = "再次输入密码不能为空")
    private String checkPassword;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 32)
    private String username;

    /**
     * 用户头像
     */
    @URL
    private String userAvatar;

    /**
     * 用户简介
     */
    @Length(max = 300)
    private String userProfile;
}
