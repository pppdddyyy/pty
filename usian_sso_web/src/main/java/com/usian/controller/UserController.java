package com.usian.controller;

import com.usian.api.UserFeign;
import com.usian.pojo.User;
import com.usian.utils.Result;
import com.usian.vo.LoginUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: UserController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/23 8:50
 */
@RestController
@RequestMapping("frontend/sso")
public class UserController {

    @Autowired
    private UserFeign userFeign;


    @RequestMapping("getUserByToken/{token}")
    public Result getUserByToken(@PathVariable("token") String token){

        User user = userFeign.getUserByToken(token);
        if(user == null){
            return Result.error("获取用户信息失败！");
        }
        return Result.ok();

    }
    @RequestMapping("userLogin")
    public Result userLogin(@RequestParam("username") String username, @RequestParam("password") String password){

       LoginUserVO loginUserVO =  userFeign.userLogin(username,password);
       if(loginUserVO==null){
          return Result.error("登录失败！！1");
       }
       return Result.ok(loginUserVO);
    }


    @RequestMapping("checkUserInfo/{checkValue}/{checkFlag}")
    public Result checkUserInfo(@PathVariable("checkValue") String checkValue, @PathVariable("checkFlag") Integer checkFlag){

       Boolean result =   userFeign.checkUserInfo(checkValue,checkFlag);
       if(result){// 可用，别人没有注册过
           return Result.ok();
       }
        return Result.error("注册过了！！！");
    }

    @RequestMapping("userRegister")
    public Result userRegister(User user){

       Boolean result =  userFeign.userRegister(user);
        if(result){// 注册成功！
            return Result.ok();
        }
        return Result.error("注册失败！！！");
    }
}
