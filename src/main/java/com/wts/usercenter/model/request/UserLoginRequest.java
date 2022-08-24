package com.wts.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录注册体
 *
 * @author wts
 */
@Data
public class UserLoginRequest implements Serializable {

    private String userAccount;
    private String userPassword;

}
