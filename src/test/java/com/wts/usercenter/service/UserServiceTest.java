package com.wts.usercenter.service;

import com.wts.usercenter.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author wts
 */

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        User user = new User();
        user.setUserName("testWts");
        user.setUserAccount("root");
        user.setAvatarUrl("https://image.baidu.com/search/albumsdetail?tn=albumsdetail&word=%E5%AE%A0%E7%89%A9%E5%9B%BE%E7%89%87&fr=albumslist&album_tab=%E5%8A%A8%E7%89%A9&album_id=688&rn=30");
        user.setGender(0);
        user.setUserPassword("root");
        user.setPhone("123456789");
        user.setEmail("123@123");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertEquals(true,result);//断言，看输出结果是不是想要的结果

    }

    @Test
    void userRegister() {
        //非空
        String userAccount = "wts";
        String userPassword = "";
        String checkPassword = "123456";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        //账户长度不小于4
        userAccount = "ts";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        //账户不能重复
        userAccount = "wts";
        userPassword = "123456";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        //账户不包含特殊字符
        userAccount = "w  ts";
        userPassword = "12345678";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        //密码和校验密码不相等
        checkPassword = "123456789";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        //密码大于等于8位
        userAccount = "newWts";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);
        //正式创建
        userAccount = "wts";
        result = userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);


    }
}