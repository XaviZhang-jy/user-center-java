package com.xavi.usercenterjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xavi.usercenterjava.model.User;
import com.xavi.usercenterjava.service.UserService;
import com.xavi.usercenterjava.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
* @author zhang
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-03-06 21:36:45
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,int gender) {
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
        final String salt = "MessiIsGoat";
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
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
}




