package com.wts.usercenter.service;

import com.wts.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

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
     * @param request
     * @return 脱敏后的用户参数
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏:脱敏就是将不重要不敏感的消息返回给前端，防止个人信息泄露
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
