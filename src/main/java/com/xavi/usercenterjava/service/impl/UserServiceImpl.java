package com.xavi.usercenterjava.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xavi.usercenterjava.constant.UserConstant;
import com.xavi.usercenterjava.model.User;
import com.xavi.usercenterjava.service.UserService;
import com.xavi.usercenterjava.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author zhang
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-03-06 21:36:45
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值
     */

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;//TODO 异常处理
        }
        if(userAccount.length() < 4){
            return -1;//TODO 异常处理
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;//TODO 异常处理
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long count = this.count(queryWrapper);
        if(count > 0){
            return -1;//TODO 异常处理
        }
        if(!checkPassword.equals(userPassword)){
            return -1;//TODO 异常处理
        }
        //账户不能包含特殊字符
        String regex = "^[a-zA-Z0-9_]+$";
        if(!userAccount.matches(regex)){
            return -1;//TODO 异常处理
        }
        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        //3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setGender(0);
        boolean saveResult = this.save(user);
        if(!saveResult){
            return -1;
        }
        return 1;
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        log.info("service: doLogin | userAccount : "+userAccount);
        //1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            log.info("user login fail. | userAccount or userPassword is blank");
            return null;//TODO 异常处理
        }
        if(userAccount.length() < 4){
            return null;//TODO 异常处理
        }
        if(userPassword.length() < 8){
            return null;//TODO 异常处理
        }
        //账户不能包含特殊字符
        String regex = "^[a-zA-Z0-9_]+$";
        if(!userAccount.matches(regex)){
            return null;//TODO 异常处理
        }
        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        //3. 校验用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if(user==null){
            log.info("user login fail. | userAccount can not match userPassword");
            return null;//TODO 异常处理
        }
        //3. 数据脱敏
        User safetyUser = new User();
        safetyUser.setUserId(user.getUserId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        //4. 记录用户信息
        httpServletRequest.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }
}




