package com.waibao.team.tuyou.event;

/**
 * Created by Delete_exe on 2016/6/2.
 */
public class LoginEvent {
    private boolean isLogin;

    public LoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
