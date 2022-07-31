package com.wts.usercenter.service;

import com.wts.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务
 *
* @author wts
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2022-07-26 15:35:23
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @return 用户列表
     */
    User doLogin(String userAccount, String userPassword);
}
