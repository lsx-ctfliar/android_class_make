package com.example.dao;

public class SignRecord {
    private int userSignId;
    private String courseId;
    private String courseAddr;
    private String createTime;

    public int getUserSignId() {
        return userSignId;
    }

    public void setUserSignId(int userSignId) {
        this.userSignId = userSignId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseAddr() {
        return courseAddr;
    }

    public void setCourseAddr(String courseAddr) {
        this.courseAddr = courseAddr;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SignRecord{" +
                "userSignId=" + userSignId +
                ", courseId='" + courseId + '\'' +
                ", courseAddr='" + courseAddr + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
