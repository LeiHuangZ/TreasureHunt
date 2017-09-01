package com.zhuoxin.treasurehunt.user.register;

/**
 * 注册请求返回的结果的Bean类
 * Created by Administrator on 2017/8/29.
 */

public class RegisterResult {

    /**
     * errcode : 1
     * errmsg : 登录成功！
     * tokenid : 171
     */

    private int errcode;
    private String errmsg;
    private int tokenid;

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public int getTokenid() {
        return tokenid;
    }
}
