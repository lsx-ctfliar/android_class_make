package com.example.util;

import com.example.data.ClassData;
import com.example.data.ClassRecords;

import java.util.LinkedList;
import java.util.List;

public class Allcourse {
    private ClassData classData;


    public void setClassData() {
        classData = new ClassData();

        classData.setCurrent(1);
        classData.setSize(10);
        classData.setTotal(2);
        List<ClassRecords> classDataList = new LinkedList<>();
//        classData.setRecords(classDataList);
        ClassRecords classRecords1 = new ClassRecords();
        ClassRecords classRecords2 = new ClassRecords();

        classRecords1.setCourseId("61");
        classRecords1.setCourseName("语文");
        classRecords1.setCoursePhoto("https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/09/04/a461932f-b2e7-4f1c-bfb1-2c26a3283c5a.jpg");
        classRecords1.setCollegeName("桂电");

        classRecords2.setCourseId("60");
        classRecords2.setCourseName("数学");
        classRecords2.setCoursePhoto("https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/09/04/a461932f-b2e7-4f1c-bfb1-2c26a3283c5a.jpg");
        classRecords2.setCollegeName("桂电");

        classDataList.add(classRecords1);
        classDataList.add(classRecords2);
    }

    public ClassData getClassData() {
        setClassData();
        return classData;
    }
}
