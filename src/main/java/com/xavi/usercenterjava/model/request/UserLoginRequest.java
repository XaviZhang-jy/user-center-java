package com.xavi.usercenterjava.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 4546452603667635944L;
    private String userAccount;
    private String userPassword;
}
