package com.xavi.usercenterjava.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xavi.usercenterjava.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceImplTest {

    @Test
    void userRegister() {

        String userAccount = "xavi";
        String userPassword = "1234567890";
        String checkPassword = "1234567890";
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        userService.remove(queryWrapper);
        long result = userService.userRegister(userAccount,userPassword,checkPassword,0);
        Assertions.assertEquals(1,result);
        userAccount = "yu";
        result = userService.userRegister(userAccount,userPassword,checkPassword,0);
        Assertions.assertEquals(-1,result);
    }
    @Resource
    public UserService userService;
    @Test
    public void testAddUser(){
        User user = new User();
        user.setUserName("xavi");
        user.setUserAccount("123");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("xxx");
        user.setEmail("xxx");
        boolean result = userService.save(user);
        System.out.println(user.getUserId());
        Assertions.assertTrue(result);
        if(result){
            userService.removeById(user.getUserId());
        }
    }
}