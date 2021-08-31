package com.usian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.usian.pojo.User;
import com.usian.service.UserService;
import com.usian.util.RedisClient;
import com.usian.utils.MD5Utils;
import com.usian.vo.LoginUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Title: UserController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/23 8:56
 */
@RestController
@RequestMapping("user/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisClient redisClient;

    @RequestMapping("/getUserByToken/{token}")
    public User getUserByToken(@PathVariable("token") String token){
        User user = (User) redisClient.get(token);
        return user;

    }

    @RequestMapping("/userLogin")
    public LoginUserVO userLogin(@RequestParam("username") String username, @RequestParam("password") String password){
        //1.登录校验  select * from user where username = ?? and password = 加密（???）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("password",MD5Utils.digest(password));
        List<User> list = userService.list(queryWrapper);

        if(list.size()>=1){
            //2. 成功，  生成一个token，组装前台需要的数据返回
            String token = UUID.randomUUID().toString();
            LoginUserVO loginUserVO = new LoginUserVO();
            loginUserVO.setUsername(username);
            loginUserVO.setUserid(list.get(0).getId()+"");
            loginUserVO.setToken(token);

            //3.             将用户信息与token存储到redis ？？？？？？
            redisClient.set(token,list.get(0)); //session
            return loginUserVO;
        }
        //失败 ，返回 null结果
        return null;
    }

    @RequestMapping("userRegister")
    public Boolean userRegister(@RequestBody User user){

        Date now = new Date();
        user.setCreated(now);
        user.setUpdated(now);
        // 需要将前台传递的密码进行加密
        user.setPassword(MD5Utils.digest(user.getPassword()));
        //insert ...
        boolean save = userService.save(user);

        return save;
    }
    @RequestMapping("checkUserInfo/{checkValue}/{checkFlag}")
    public Boolean checkUserInfo(@PathVariable("checkValue") String checkValue, @PathVariable("checkFlag") Integer checkFlag){
        //1. 判断校验的类型
        if(checkFlag == 1){
            //2. 如果是 用户名   select * from user where username = ?zhangsan?
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",checkValue);

            List<User> list = userService.list(queryWrapper);
            if(list.size()>=1){
                return false;// 不能用
            }

        }else { //  如果是   手机号  select * from user where phone = ?12344xxx?
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone",checkValue);

            List<User> list = userService.list(queryWrapper);
            if(list.size()>=1){
                return false;// 不能用
            }
        }
        return true;

    }
}
