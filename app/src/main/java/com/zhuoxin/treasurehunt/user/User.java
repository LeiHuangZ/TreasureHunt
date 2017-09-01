package com.zhuoxin.treasurehunt.user;

/**
 * 登录或者注册的所用的用户信息Bean类
 * Created by Administrator on 2017/8/29.
 */

public class User {

    /**
     * UserName : qjd
     * Password : 654321
     */

    private String UserName;
    private String Password;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
}
