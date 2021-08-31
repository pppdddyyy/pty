package com.usian.vo;

/**
 * @Title: LoginUserVO
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/23 9:40
 */
public class LoginUserVO {

    private String token;
    private String userid;
    private String username;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
