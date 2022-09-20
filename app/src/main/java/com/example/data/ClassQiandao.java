package com.example.data;

import java.util.ArrayList;

public class ClassQiandao {
    private ArrayList<Records> records;
    private int total;
    private int size;
    private int current;

    public static class Records{
        private String userSignId;
        private String courseName;
        private String courseAddr;
        private String createTime;

        public String getUserSignId() {
            return userSignId;
        }

        public void setUserSignId(String userSignId) {
            this.userSignId = userSignId;
        }

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return "Records{" +
                    "userSignId='" + userSignId + '\'' +
                    ", courseName='" + courseName + '\'' +
                    ", courseAddr='" + courseAddr + '\'' +
                    ", createTime='" + createTime + '\'' +
                    '}';
        }
    }

    public ArrayList<Records> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Records> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "ClassQiandao{" +
                "records=" + records +
                ", total=" + total +
                ", size=" + size +
                ", current=" + current +
                '}';
    }
}
