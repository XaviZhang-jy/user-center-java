package com.xavi.usercenterjava.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xavi.usercenterjava.constant.UserConstant;
import com.xavi.usercenterjava.model.User;
import com.xavi.usercenterjava.model.request.UserLoginRequest;
import com.xavi.usercenterjava.model.request.UserRegisterRequest;
import com.xavi.usercenterjava.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class userController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest 注册请求
     * @return 状态值
     */
    @PostMapping(value = "/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest==null){
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    /**
     * 用户登录
     * @param userLoginRequest 登录请求
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest){
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        return userService.doLogin(userAccount, userPassword, httpServletRequest);
    }

    /**
     * 查询用户
     * @param username 用户名
     * @return
     */
    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest request){
        if(!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isBlank(username)){
            queryWrapper.like("user_name",username);
        }
        List<User> lists = userService.list(queryWrapper);
        return lists.stream().map(user -> userService.getSafetyUser(user)
        ).collect(Collectors.toList());
    }

    /**
     * 根据ID删除用户（逻辑删除）
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request){
        if(id<=0||!isAdmin(request)){
            return false;
        }
        return userService.removeById(id);
    }

    private boolean isAdmin(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        //管理员权限判断+判空
        return user != null && user.getUserRole() == UserConstant.XAVI_ROLE;
    }
}
