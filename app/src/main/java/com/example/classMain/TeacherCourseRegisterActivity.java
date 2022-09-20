package com.example.classMain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dao.CourseRegister;
import com.example.picturesharing.R;
import com.example.util.MyPost;
import com.example.util.MyResponse;
import com.example.util.MyUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TeacherCourseRegisterActivity extends AppCompatActivity {
    private static final String TAG = "TeacherCourseRegisterActivity";
    int courseId=88,userId,role;
    public static String starTime = "请选择时间";
    public static String endTime = "请选择时间";
    public static long enTime ;
    public static long stTime ;
    TextView et_endTime;
    TextView et_startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取全局userid和roleid
        BottomBarActivity bottomBarActivity; //获取主活动中的对象
//        role = BottomBarActivity.getUser().getRoleId();
        userId = Integer.parseInt(BottomBarActivity.getUser().getId());
        courseId = BottomBarActivity.getCourseId();
        System.out.println("-------------------------------------------------"+courseId+"userid: "+userId);

        setContentView(R.layout.course_register);
        EditText et_courseName = (EditText) this.findViewById(R.id.courseName);
        EditText et_courseAddr = (EditText) this.findViewById(R.id.courseAddr);
        EditText et_signCode = (EditText) this.findViewById(R.id.signCode);
        EditText et_total = (EditText) this.findViewById(R.id.total);
        Button but = (Button) this.findViewById(R.id.button);
        et_startTime = (TextView) this.findViewById(R.id.et_startTime);
        et_endTime = (TextView) this.findViewById(R.id.et_endTime);
        //设置开始时间
        et_startTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("即将跳转到timePicker");
                        Intent intent = new Intent(TeacherCourseRegisterActivity.this, timeStartActivity.class);
                        startActivity(intent);
                        System.out.println("跳转到timePicker");
                    }
                }
        );

        //设置结束时间
        et_endTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TeacherCourseRegisterActivity.this,timeEndActivity.class);
                        startActivity(intent);
                    }
                }
        );

        but.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String courseName = et_courseName.getText().toString().trim();
                        String courseAddr = et_courseAddr.getText().toString().trim();
                        int signCode = Integer.parseInt(et_signCode.getText().toString().trim());
                        int total = Integer.parseInt(et_total.getText().toString().trim());
                        System.out.println("sttime"+stTime);
                        CourseRegister courseRegister = new CourseRegister();
                        courseRegister.setCourseName(courseName);
                        courseRegister.setCourseAddr(courseAddr);
                        courseRegister.setCourseId(courseId);
                        courseRegister.setUserId(userId);
                        courseRegister.setTotal(total);
                        courseRegister.setSignCode(signCode);
                        courseRegister.setBeginTime(stTime);
                        courseRegister.setEndTime(enTime);
                        System.out.println("course"+courseRegister.getCourseName());
                        System.out.println("courseid"+courseRegister.getCourseId());
                        Request request = MyPost.post(courseRegister, "/sign/course/teacher/initiate");
                        Call call = MyUtil.getCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e(TAG, "onFailure" + e.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "签到发布失败!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            //成功
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                Log.e(TAG, "onResponse " + response.code());
                                String body = response.body().string();
                                Log.e(TAG, "onResponse: " + body);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (MyResponse.getCode(body).equals("200")) {
                                            Toast.makeText(getApplicationContext(), "签到发布成功", Toast.LENGTH_SHORT).show();
                                            // 跳转到登录界面
//                                            Intent intent = new Intent(TeacherCourseRegisterActivity.this, TeacherCourseDetailActivity.class);
//                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "服务器错误", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        });
                    }
                }
        );
    }
    @Override       //这里是实现了自动更新
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        et_startTime.setText(starTime);
        et_endTime.setText(endTime);
    }

}
