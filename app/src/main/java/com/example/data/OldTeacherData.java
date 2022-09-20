package com.example.data;

import android.app.Application;

import java.util.ArrayList;

public class OldTeacherData extends Application {
    private int total;
    private int size;
    private int current;
    public ArrayList<Records> records;

    public static class Records{
        private String courseId;
        private String courseName;
        private String coursePhoto;
        private String collegeName;

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
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

        @Override
        public String toString() {
            return "Records{" +
                    "courseId='" + courseId + '\'' +
                    ", courseName='" + courseName + '\'' +
                    ", coursePhoto='" + coursePhoto + '\'' +
                    ", collegeName='" + collegeName + '\'' +
                    '}';
        }
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

    public ArrayList<Records> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Records> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "RecentTeacherData{" +
                "total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", records=" + records +
                '}';
    }
}
