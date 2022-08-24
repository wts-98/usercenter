package com.wts.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户请求注册体
 *
 * @author wts
 */
@Data
public class UserRegisterRequest implements Serializable {

    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
