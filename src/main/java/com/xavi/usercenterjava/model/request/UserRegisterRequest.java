package com.xavi.usercenterjava.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Getter
@Setter
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -6772075115605119063L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
