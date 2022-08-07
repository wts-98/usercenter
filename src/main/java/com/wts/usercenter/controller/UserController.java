package com.wts.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wts.usercenter.model.User;
import com.wts.usercenter.model.request.UserLoginRequest;
import com.wts.usercenter.model.request.UserRegisterRequest;
import com.wts.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wts.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.wts.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author wts
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            return null;
        }
        String userName = userRegisterRequest.getUserName();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userName,userPassword,checkPassword)){
            return null;
        }
        return userService.userRegister(userName,userPassword,checkPassword);
    }

    @PostMapping("/register")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            return null;
        }
        String userName = userLoginRequest.getUserName();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userName,userPassword)){
            return null;
        }
        return userService.userLogin(userName,userPassword,request);
    }

    @GetMapping("/search")
    public List<User> searchUser(String userName,HttpServletRequest request){
        if (isAdmin(request)){
            return new ArrayList<>();
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)){
            queryWrapper.like("userName",userName);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null){
            return null;
        }
        Long userId = currentUser.getId();
        //TODO: 检验用户是否合法
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }

    @PostMapping("/delete")
    public Boolean deleteUser(@RequestBody Long id,HttpServletRequest request){
        if (!isAdmin(request)){
            return false;
        }
        if (id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        //仅管理员可以查看
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;//强转
        return user == null || user.getUserRole() != ADMIN_ROLE;
    }
}
