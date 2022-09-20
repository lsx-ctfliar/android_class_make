package com.example.dao;

public class CourseRegister {
    private String courseName;
    private String courseAddr;
    private long beginTime;
    private long endTime;
    private int signCode;
    private int total;
    private int userId;
    private int courseId;
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseAddr() {
        return courseAddr;
    }

    public void setCourseAddr(String courseAddr) {
        this.courseAddr = courseAddr;
    }

    public long getbeginTime() {
        return beginTime;
    }

    public void setBeginTime(long startTime) {
        this.beginTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getSignCode() {
        return signCode;
    }

    public void setSignCode(int signCode) {
        this.signCode = signCode;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }




}
