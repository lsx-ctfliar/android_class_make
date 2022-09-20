package com.example.dao;

public class DetailCourse {
    private String id;
    private String courseName;
    private String coursePhoto;
    private String collegeName;
    private String introduce;
    private String startTime;
    private String endTime;
    private String userName;
    private String realName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoursePhoto() {
        return coursePhoto;
    }

    public void setCoursePhoto(String coursePhoto) {
        this.coursePhoto = coursePhoto;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String createTime;
    private boolean hasSelect;


    public boolean isHasSelect() {
        return hasSelect;
    }

    public void setHasSelect(boolean hasSelect) {
        this.hasSelect = hasSelect;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCourseName() {
        System.out.println(courseName);
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
//        System.out.println(courseName);
    }

    @Override
    public String toString() {
        return "Course{" +
                "Id='" + id + '\'' +
                ", courseName='" + courseName + '\'' +
                ", coursePhoto='" + coursePhoto + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", introduce='" + introduce + '\'' +
                ", startTime=" + startTime +'\''+
                ", endTime=" + endTime +'\'' +
                ", userName=" + userName +'\'' +
                ", realName=" +realName + '\'' +
                '}';
    }
}
