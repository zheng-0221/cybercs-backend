package com.zpp.cybercs.user.service.impl;

import java.util.ArrayList;
import java.util.Date;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zpp.cybercs.common.ErrorCode;
import com.zpp.cybercs.constant.CommonConstant;
import com.zpp.cybercs.user.model.dto.UserQueryRequest;
import com.zpp.cybercs.user.util.UserHolder;
import com.zpp.cybercs.exception.BusinessException;
import com.zpp.cybercs.exception.ThrowUtils;
import com.zpp.cybercs.user.model.User;
import com.zpp.cybercs.user.model.dto.UserLoginRequest;
import com.zpp.cybercs.user.model.dto.UserRegisterRequest;
import com.zpp.cybercs.user.model.dto.UserUpdateRequest;
import com.zpp.cybercs.user.model.vo.SafetyUser;
import com.zpp.cybercs.user.mapper.UserMapper;
import com.zpp.cybercs.user.service.UserService;
import com.zpp.cybercs.util.RedisUtils;
import com.zpp.cybercs.util.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.zpp.cybercs.constant.RedisKeys.LOGIN_USER_KEY;
import static com.zpp.cybercs.constant.RedisKeys.LOGIN_USER_TTL;

/**
 * @author zheng
 * @description 针对表【tb_user(用户表)】的数据库操作Service实现
 * @createDate 2023-11-04 16:13:04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "zpp";

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public SafetyUser register(UserRegisterRequest userRegisterRequest) {

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();

        // 保证账户不能重复
        // TODO 多实例部署的情况下，要使用分布式锁
        synchronized (userAccount.intern()) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserAccount, userAccount);
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            userRegisterRequest.setUserPassword(encryptPassword);
            // 3. 插入数据
            User user = BeanUtil.copyProperties(userRegisterRequest, User.class);
            boolean saveFail = !(this.save(user));
            ThrowUtils.throwIf(saveFail, ErrorCode.SYSTEM_ERROR, "注册失败");

            return UserHolder.toSafety(user);
        }
    }

    @Override
    public String login(UserLoginRequest userLoginRequest) {

        String loginAccount = userLoginRequest.getUserAccount();

        // 1. 验证账户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, loginAccount);
        User user = userMapper.selectOne(queryWrapper);

        // 1.1 账户是否存在
        ThrowUtils.throwIf(ObjectUtils.isEmpty(user), ErrorCode.NOT_FOUND_ERROR, "账户不存在");

        // 1.2 验证密码
        String loginPassword = DigestUtils.md5DigestAsHex((SALT + userLoginRequest.getUserPassword()).getBytes());
        String userPassword = user.getUserPassword();
        boolean passwordError = !StringUtils.equals(loginPassword, userPassword);
        ThrowUtils.throwIf(passwordError, ErrorCode.PARAMS_ERROR, "密码错误");

        // 2. 保存用户信息到 redis中
        // 2.1 随机生成token，作为登录令牌
        String baseToken = UUID.randomUUID().toString(true);
        String token = LOGIN_USER_KEY + baseToken;

        // 2.2 将 User 对象脱敏，并转为 HashMap 存储
        SafetyUser safetyUser = UserHolder.toSafety(user);
        RedisUtils.toHash(stringRedisTemplate, token, safetyUser, LOGIN_USER_TTL, TimeUnit.SECONDS);

        // 2.3 保存登录状态
        UserHolder.saveLoginUser(safetyUser);

        // 3. 返回token
        return baseToken;
    }

    @Override
    public SafetyUser getCurrentUser() {

        SafetyUser currentUser = UserHolder.getCurrentUser();
        ThrowUtils.throwIf(ObjectUtils.isEmpty(currentUser), ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    @Override
    public void updateInfo(UserUpdateRequest userUpdateRequest) {

        User user = BeanUtil.copyProperties(userUpdateRequest, User.class);
        int result = userMapper.updateById(user);
        ThrowUtils.throwIf(result < 1, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public SafetyUser getSafetyUserById(long id) {

        User user = userMapper.selectById(id);
        SafetyUser safetyUser = UserHolder.toSafety(user);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(safetyUser), ErrorCode.SYSTEM_ERROR);
        return safetyUser;
    }

    @Override
    public Page<SafetyUser> listSafetyUser(UserQueryRequest userQueryRequest) {

        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();

        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<User> userPage = userMapper.selectPage(new Page<>(current, size),
                this.getQueryWrapper(userQueryRequest));
        Page<SafetyUser> safetyUserPage = new Page<>(current, size);
        List<SafetyUser> userVO = this.toSafetyList(userPage.getRecords());
        safetyUserPage.setRecords(userVO);
        return null;
    }

    @Override
    public Page<User> listUser(UserQueryRequest userQueryRequest) {

        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        return userMapper.selectPage(new Page<>(current, size), this.getQueryWrapper(userQueryRequest));
    }


    // 分页查询用户，条件封装工具
    private QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        String username = userQueryRequest.getUsername();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "user_profile", userProfile);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "user_role", userRole);
        queryWrapper.orderBy(
                SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder),
                sortField);
        return queryWrapper;
    }

    // 将 List<User> 脱敏为 List<SafetyUser>
    private List<SafetyUser> toSafetyList(List<User> userList) {

        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(UserHolder::toSafety).collect(Collectors.toList());
    }

}
