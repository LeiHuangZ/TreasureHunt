package com.zhuoxin.treasurehunt.user.login;

/**
 * 登录的返回结果的Bean类
 * Created by Administrator on 2017/8/29.
 */

public class LoginResult {

    /**
     * errcode : 1
     * errmsg : 登录成功！
     * headpic : add.jpg
     * tokenid : 171
     */

    private int errcode = 0;
    private String errmsg = "";
    private String headpic = "";
    private int tokenid = 0;

    int getErrcode() {
        return errcode;
    }

    String getErrmsg() {
        return errmsg;
    }


    String getHeadpic() {
        return headpic;
    }

    int getTokenid() {
        return tokenid;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", headpic='" + headpic + '\'' +
                ", tokenid=" + tokenid +
                '}';
    }
}
