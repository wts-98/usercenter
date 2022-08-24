package com.wts.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wts.usercenter.common.ErrorCode;
import com.wts.usercenter.exception.BusinessException;
import com.wts.usercenter.model.User;
import com.wts.usercenter.service.UserService;
import com.wts.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wts.usercenter.contant.UserConstant.USER_LOGIN_STATE;

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
     * 盐值，混淆密码
     */
    public static final String SALT = "wts";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验不为空
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"参数为空");
        }
        //账户不小于4
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"用户账号过短");
        }
        //密码不小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"用户密码过短");
        }
        //账户不包含特殊字符
        String validPattern="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"用户账户包含特殊字符");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"校验密码错误");
        }
        //账户不能重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"用户账号重复");
        }
        //2.加密密码用MD5
        String encryptPassword = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        //3.向数据库插入用户数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"用户账号重复");
        }
        return 0;
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验不为空
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"参数为空");
        }
        //账户不小于4
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"用户账号过短");
        }
        //密码不小于8位
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"用户密码过短");
        }
        //账户不包含特殊字符
        String validPattern="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARANS_ERROR,"用户账户包含特殊字符");
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
        //3.用户脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null){
            return null;
        }
        //3.用户脱敏--为了返回特定的数据，不让前端直接查看到信息
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        //safetyUser.setUserPassword("");不需要让前端知道密码
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());

        return safetyUser;

    }

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




