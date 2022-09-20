package com.example.classMain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.UriUtils;
import com.example.dao.AddCourse;
import com.example.data.ClassData;
import com.example.data.RecentTeacherData;
import com.example.picturesharing.R;
import com.example.util.MyPost;
import com.example.util.MyResponse;
import com.example.util.MyUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

// 教师添加课程
public class AddCourseActivity extends AppCompatActivity implements  View.OnClickListener{
    private static final String TAG = "AddCourseActivity";
    private static final int REQUEST_CODE = 0x00000011;
    public static String starTime = "请选择时间";
    public static String endTime = "请选择时间";
    int userId;
    EditText et_courseName;
    EditText et_collegeName;
    TextView et_startTime;
    TextView et_endTime;
    EditText et_introduce;
    ImageView coursePhoto;
    Button submit;
    String photoUrl;
    File photofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取全局userid
        userId = Integer.parseInt(BottomBarActivity.getUser().getId());

        setContentView(R.layout.activity_add_course);

        et_courseName = (EditText) this.findViewById(R.id.et_courseName);
        et_collegeName = (EditText) this.findViewById(R.id.et_collegeName);
        et_startTime = this.findViewById(R.id.et_startTime);
        et_endTime = findViewById(R.id.et_endTime);
        et_introduce = (EditText) this.findViewById(R.id.et_introduce);
        coursePhoto = (ImageView) this.findViewById(R.id.et_coursePhoto);
        submit = (Button) this.findViewById(R.id.bt_addCourse);

        //添加文件
        coursePhoto.setOnClickListener(this);
        //设置开始时间
        et_startTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddCourseActivity.this, StarttimeActivity.class);
                        startActivity(intent);
                    }
                }
        );

        //设置结束时间
        et_endTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddCourseActivity.this, EndtimeActivity.class);
                        startActivity(intent);
                    }
                }
        );

        //todo 添加异步请求课程
        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "等待数据加载", Toast.LENGTH_LONG).show();
                        Request requestFile = MyPost.postFile(getPhotofile(),"/sign/image/upload");
                        Call call = MyUtil.getCall(requestFile);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e(TAG,e.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "网络连接失败!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                Log.e(TAG, "onResponse " + response.code());
                                String body = response.body().string();
                                Log.e(TAG, "onResponse: " + body);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (MyResponse.getCode(body).equals("200")) {
                                            Toast.makeText(getApplicationContext(), "正在新增课程", Toast.LENGTH_LONG).show();
                                            int index1 = body.indexOf("http");
                                            int index2 = body.length()-2;
                                            setPhotoUrl(body.substring(index1,index2));

                                            //添加课程
                                            String courseName = et_courseName.getText().toString().trim();
                                            String collegeName = et_collegeName.getText().toString().trim();
                                            String introduce = et_introduce.getText().toString().trim();


                                            //接口要求的时间戳类型是Integet(int64)
                                            int stTime = Integer.parseInt(starTime.replace("年","").replace("月","").replace("日",""));
                                            int enTime = Integer.parseInt(endTime.replace("年","").replace("月","").replace("日",""));
                                            photoUrl = getPhotoUrl();
                                            System.out.println("发布课程的开始和结束时间------- "+stTime+enTime);


                                            AddCourse addCourse = new AddCourse();
                                            addCourse.setCourseName(courseName);
                                            addCourse.setCollegeName(collegeName);
                                            addCourse.setIntroduce(introduce);

                                            //写死图片链接
                                            //addCourse.setCoursePhoto("https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/10/20/b3c53316-81c7-4cd5-a366-40e2067c00ae.png");
                                            addCourse.setCoursePhoto(photoUrl);
                                            addCourse.setRealName(BottomBarActivity.getUser().getRealName());

                                            addCourse.setStartTime(stTime);
                                            addCourse.setEndTime(enTime);

                                            addCourse.setUserName(BottomBarActivity.getUser().getUserName());
                                            addCourse.setUserId(userId);

                                            Request request = MyPost.post(addCourse, "/sign/course/teacher");
                                            Call call = MyUtil.getCall(request);
                                            call.enqueue(new Callback() {
                                                @Override
                                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                    Log.e(TAG, "onFailure" + e.toString());
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getApplicationContext(), "课程新增失败!", Toast.LENGTH_SHORT).show();
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
                                                                Toast.makeText(getApplicationContext(), "课程新增成功", Toast.LENGTH_SHORT).show();
                                                                // 更新主页数据
                                                                String param = "/sign/course/all?current="+1+"&size="+100;
                                                                Request request = MyUtil.get(param);
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
                                                                            ClassData classData1 = MyUtil.getJsonObject(responseJsonString,ClassData.class);
                                                                            Log.e(TAG,"classData1.string " + classData1.toString());
                                                                            BottomBarActivity.classData.setCurrent(classData1.getCurrent());
                                                                            BottomBarActivity.classData.setSize(classData1.getSize());
                                                                            BottomBarActivity.classData.setTotal(classData1.getTotal());
                                                                            BottomBarActivity.classData.setRecords(classData1.getRecords());
                                                                        }
                                                                        finish();
                                                                    }
                                                                });
                                                                // 更新未接课的数据
                                                                String param2 = "/sign/course/teacher/unfinished?current="+1+"&size="+100+"&userId="+userId;
                                                                Request request2 = MyUtil.get(param2);
                                                                System.out.println(request.url());
                                                                Call call2 = MyUtil.getCall(request2);
                                                                call2.enqueue(new Callback() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                                        Log.e(TAG,"onFailure"+e.toString());
                                                                        // todo 没有课程就给一个提示数据

                                                                        runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                Toast.makeText(getApplicationContext(), "网络问题，课程数据加载!", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                                        ResponseBody responseBody =response.body();
                                                                        String responseJsonString = responseBody.string();
                                                                        Log.e(TAG,"getTeacherData recent onResponse " + response.code());
                                                                        Log.e(TAG,"getTeacherData recent onResponse " + responseJsonString);
                                                                        if(MyResponse.getCode(responseJsonString).equals("200")){
                                                                            RecentTeacherData recentTeacherDataTemp = MyUtil.getJsonObject(responseJsonString,RecentTeacherData.class);
                                                                            BottomBarActivity.recentTeacherData.setRecords(recentTeacherDataTemp.getRecords());
                                                                            BottomBarActivity.recentTeacherData.setSize(recentTeacherDataTemp.getSize());
                                                                            BottomBarActivity.recentTeacherData.setTotal(recentTeacherDataTemp.getTotal());
                                                                            BottomBarActivity.recentTeacherData.setCurrent(recentTeacherDataTemp.getCurrent());
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                            else {
                                                                Toast.makeText(getApplicationContext(), "已出现系统课程名", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "图片加载错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
    }





    @Override
    public void onClick( View v){
        switch (v.getId()){
            case R.id.et_coursePhoto:
                ImageSelector.builder()
                        .useCamera(true)
                        .setSingle(true)
                        .canPreview(true)
                        .start(this,REQUEST_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE && data != null){
            ArrayList<String> images= data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            coursePhoto.setImageURI(UriUtils.getImageContentUri(getApplicationContext(), images.get(0)));
            setPhotoUrl(images.get(0));
            setPhotofile(getPhotoUrl());
        }
    }

    @Override       //这里是实现了自动更新
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        et_startTime.setText(starTime);
        et_endTime.setText(endTime);
    }



    public File getPhotofile() {
        return photofile;
    }
    //获取照片路径
    public String getPhotoUrl(File photofile) {
        return photoUrl;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public void setPhotofile(String photoUrl) {
        this.photofile = new File(photoUrl);
    }
}
