package com.usian.interceptor;

import com.usian.pojo.User;
import com.usian.util.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title: LoginInterceptor
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/24 14:47
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisClient redisClient;
    /**
     * 前置的拦截
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断用户是否登录过,登录之后，给用户端颁发了token
        String token = request.getParameter("token");

        if(token == null || token.isEmpty()){
            return false;// true 放行  false 拦截
        }else{
            // 判断是否是有效的token
            User user = (User) redisClient.get(token);
            if(user == null){
                return false;//token 过期了 拦截
            }
            return  true;// 登录过  放行
        }
    }
}
