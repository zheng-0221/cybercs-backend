package com.zpp.cybercs.user.model.dto;

import com.zpp.cybercs.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zpp
 * @version 1.0
 * @description 管理员分页查询用户请求类
 * @date 2023/11/9 22:13:42
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 9067021810581632203L;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/vip/admin/banned
     */
    private String userRole;

}
