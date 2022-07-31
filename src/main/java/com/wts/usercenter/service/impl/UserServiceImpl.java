package com.wts.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wts.usercenter.model.User;
import com.wts.usercenter.service.UserService;
import com.wts.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
 *
 * @author wts
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    UserMapper userMapper;

    /**
     *
     * 加盐
     */
    public static final String SALT = "wts";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验不为空
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        //账户不小于4
        if (userAccount.length() < 4){
            return -1;
        }
        //密码不小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }
        //账户不包含特殊字符
        String validPattern="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return -1;
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)){
            return -1;
        }
        //账户不能重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if (count > 0){
            return -1;
        }
        //2.加密密码用MD5
        final String SALT = "wts";
        String encryptPassword = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        //3.向数据库插入用户数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult){
            return -1;
        }
        return 0;
    }

    @Override
    public User doLogin(String userAccount, String userPassword) {
        //1.校验不为空
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        //账户不小于4
        if (userAccount.length() < 4){
            return null;
        }
        //密码不小于8位
        if (userPassword.length() < 8){
            return null;
        }
        //账户不包含特殊字符
        String validPattern="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return null;
        }
        //2.加密密码用MD5
        String encryptPassword = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        //查找第一条语句
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null){
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }
        //3.记录用户的登录态

        return user;
    }
}




