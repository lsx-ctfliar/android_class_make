package com.example.classMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dao.DetailCourse;
import com.example.picturesharing.R;
import com.example.util.MyResponse;
import com.example.util.MyUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TeacherCourseDetailActivity extends Activity {
    public static DetailCourse detailCourse;
    public static final String TAG = "TeacherCourseDetailActivity";
    int courseId;
    int userId;
    int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_detail);
        Button but = (Button) findViewById(R.id.bt_addRegister);
        TextView courseName = (TextView) findViewById(R.id.dcourse_name);
        TextView dcourseId = (TextView) findViewById(R.id.dcourse_id);
        TextView collegeName = (TextView) findViewById(R.id.dschool);
        TextView introduce = (TextView) findViewById(R.id.dcourse_content);
        //获取全局userid和roleid
        role = BottomBarActivity.getUser().getRoleId();
        userId = Integer.parseInt(BottomBarActivity.getUser().getId());
        courseId = BottomBarActivity.getCourseId();
        courseName.setText(detailCourse.getCourseName());
        dcourseId.setText(detailCourse.getId());
        collegeName.setText(detailCourse.getCollegeName());
        introduce.setText(detailCourse.getIntroduce());
        but.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TeacherCourseDetailActivity.this, TeacherCourseRegisterActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }


    public DetailCourse getDetailCourse() {
        return detailCourse;
    }

    public void setCourse(DetailCourse detailCourse){
        detailCourse = detailCourse;
    }

    //页面刷新
    public void flush() {
        finish();
        Intent intent = new Intent(this, DetailCourseActivity.class);
        startActivity(intent);
    }

    //调用接口获取课程详情页面
    public void getCourse(int get_courseId,int get_userId)
    {
        String param = "/sign/course/detail?courseId="+get_courseId+"&userId="+get_userId;
        Request request = MyUtil.get(param);
        System.out.println(request);
        Call call = MyUtil.getCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"onFailure"+e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络问题，课程数据加载!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e(TAG,"onResponse " + response.code());
                String responseJsonString = response.body().string();
                Log.e(TAG,"onResponse " + responseJsonString);
                if(MyResponse.getCode(responseJsonString).equals("200")){
                    DetailCourse detailCourse1 = MyUtil.getJsonObject(responseJsonString,DetailCourse.class);
                    Log.e(TAG,"detailCourse1.string " + detailCourse1.toString());
                    //拿到课程数据后传入DetailCourse
                    detailCourse = detailCourse1;
                    flush();
//                    //添加成功后页面跳转
//                    Intent intent = new Intent(context, DetailCourseActivity.class);
//                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), responseJsonString, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
