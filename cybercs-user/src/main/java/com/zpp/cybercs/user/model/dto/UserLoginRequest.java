package com.zpp.cybercs.user.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author zpp
 * @version 1.0
 * @description 用户登录请求类
 * @date 2023/11/5 19:21:35
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3435078334232405209L;
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
//    @Pattern(message = "密码由 6 - 22 位数字和英文混合组成", regexp = "/^[a-zA-Z0-9]{6,22}$/")
    @Length(message = "密码最短为 6 位，最长为 22 位", min = 6, max = 22)
    private String userPassword;
}
