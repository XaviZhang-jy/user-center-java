package com.xavi.usercenterjava.service;

import com.xavi.usercenterjava.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Xavi
* @author zhang
* @description 针对表【user】的数据库操作Service
* @createDate 2023-03-06 21:36:45
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 状态值
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 用户信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);
}
