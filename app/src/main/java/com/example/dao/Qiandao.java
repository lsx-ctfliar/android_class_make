package com.example.dao;

public class Qiandao {
    private int signCode;  //签到码
    private int userId;   //当前登录用户的主键id
    private int userSignId;  //学生获取到的签到表主键id


    public int getSignCode() {
        return signCode;
    }

    public void setSignCode(int signCode) {
        this.signCode = signCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserSignId() {
        return userSignId;
    }

    public void setUserSignId(int userSignId) {
        this.userSignId = userSignId;
    }

    @Override
    public String toString() {
        return "Qiandao{" +
                "signCode=" + signCode +
                ", userId=" + userId +
                ", userSignId=" + userSignId +
                '}';
    }
}
