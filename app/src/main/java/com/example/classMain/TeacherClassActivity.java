package com.example.classMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.adapter.OldCourseAdapter;
import com.example.adapter.RecentCourseAdapter;
import com.example.data.OldTeacherData;
import com.example.data.RecentTeacherData;
import com.example.picturesharing.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherClassActivity extends Fragment {
    private static final String TAG = "TeacherClassActivity";
    private ListView recentCourseList;
    private ListView oldCourseList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity(); //获取主活动中的对象
        View view = inflater.inflate(R.layout.activity_teacher_class,container,false);
        recentCourseList = view.findViewById(R.id.recently_courses);
        oldCourseList = view.findViewById(R.id.old_courses);



        initUi();




        return view;
    }

    public void initUi(){

        //获取主活动中的对象
        BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity();

        //获取教师未结课课程数据
        RecentTeacherData recentTeacherData = bottomBarActivity.getRecentTeacherData();
        while (recentTeacherData.getSize()==0){
            recentTeacherData = bottomBarActivity.getRecentTeacherData();
        }

        if(recentTeacherData.getRecords()!=null)
        {
            List<Map<String,Object>> recentList = getRecentList(recentTeacherData);
            recentCourseList.setAdapter(new RecentCourseAdapter(getContext(),recentList));
        }

        //获取教师结课课程数据
        OldTeacherData oldTeacherData = bottomBarActivity.getOldTeacherData();
        while (oldTeacherData.getSize()==0){
            oldTeacherData = bottomBarActivity.getOldTeacherData();
        }

        if (oldTeacherData.getRecords()!=null)
        {
            List<Map<String,Object>> oldList = getOldList(oldTeacherData);
            oldCourseList.setAdapter(new OldCourseAdapter(getContext(),oldList));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initUi();
    }

    public List<Map<String,Object>> getRecentList(RecentTeacherData recentTeacherData){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < (recentTeacherData.getRecords()).size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("coursePhoto",recentTeacherData.getRecords().get(i).getCoursePhoto());
            map.put("courseName", recentTeacherData.getRecords().get(i).getCourseName());
            map.put("collegeName", recentTeacherData.getRecords().get(i).getCollegeName());
            map.put("courseId", recentTeacherData.getRecords().get(i).getCourseId());
            list.add(map);
        }
        return list;
    }

    public List<Map<String,Object>> getOldList(OldTeacherData oldTeacherData){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < (oldTeacherData.getRecords()).size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("coursePhoto",oldTeacherData.getRecords().get(i).getCoursePhoto());
            map.put("courseName", oldTeacherData.getRecords().get(i).getCourseName());
            map.put("collegeName", oldTeacherData.getRecords().get(i).getCollegeName());
            map.put("courseId", oldTeacherData.getRecords().get(i).getCourseId());
            list.add(map);
        }
        return list;
    }

    //todo
    public List<Map<String,Object>> getTestList(){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 12; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("coursePhoto","https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/09/08/681dbdfc-df32-4d71-a7d2-ce4a4e1a2bba.png");
            map.put("courseName", "courseName");
            map.put("collegeName", "gidian");
            map.put("courseId", "255");
            list.add(map);
        }
        return list;
    }

}
