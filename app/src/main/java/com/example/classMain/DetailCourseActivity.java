package com.example.classMain;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dao.DetailCourse;
import com.example.dao.User;
import com.example.picturesharing.R;
import com.example.util.MyResponse;
import com.example.util.MyUtil;
import android.content.Intent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


//课程详情页面，学生有加入按钮
public class DetailCourseActivity extends AppCompatActivity {
    private User user;
    public static DetailCourse course;
    int courseId,userId,role;
    String createtime;
    String endtime;
    String startime;
    public static final String TAG = "DetailCourseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_course);
        TextView courseName = (TextView) findViewById(R.id.dcourse_name);
        TextView dcourseId = (TextView) findViewById(R.id.dcourse_id);
        TextView collegeName = (TextView) findViewById(R.id.dschool);
        TextView introduce = (TextView) findViewById(R.id.dcourse_content);
        TextView startTime = (TextView) findViewById(R.id.starttime);
        TextView endTime = (TextView) findViewById(R.id.endtime);
        TextView realName = (TextView) findViewById(R.id.realname);
        TextView userName = (TextView) findViewById(R.id.username);
        TextView createTime = (TextView) findViewById(R.id.createtime);
        Button b = (Button)findViewById(R.id.dadd_course);
        TextView addcourse = (TextView) findViewById(R.id.addText);


        //获取全局userid和roleid
        role = BottomBarActivity.getUser().getRoleId();
        userId = Integer.parseInt(BottomBarActivity.getUser().getId());
//        courseId = BottomBarActivity.getCourseId();
//        getCourse(courseId,userId);

//
        //todo 获取到课程数据后跳出
        while (course==null){
            course = getDetailCourse();
            System.out.println("///////////");
        }

        courseId = Integer.parseInt( course.getId());

        createtime = DateChange(Long.parseLong(course.getCreateTime()));
        endtime = DateChange(Long.parseLong(course.getEndTime()));
        startime = DateChange(Long.parseLong(course.getStartTime()));

        courseName.setText(course.getCourseName());
        dcourseId.setText(course.getId());
        collegeName.setText(course.getCollegeName());
        introduce.setText(course.getIntroduce());
        startTime.setText(startime);
        endTime.setText(endtime);
        realName.setText(course.getRealName());
        userName.setText(course.getUserName());
        createTime.setText(createtime);


        //false未选课，true已选课,0是学生，1是老师
        if(!course.isHasSelect()&&role==0)//学生未选课，添加按钮显示
        {
            b.setVisibility(View.VISIBLE);
        }
        else if(role==0&&course.isHasSelect()) //学生已选课
        {
            addcourse.setVisibility(View.VISIBLE);
        }

        //点击按钮添加课程
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestBody requestBody = MyUtil.getBuilder()
                                .add("courseId",courseId+"")
                                .add("userId",userId+"")
                                .build();
                        Request request = MyUtil.post(requestBody,"/sign/course/student/select");
                        Call call = MyUtil.getCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e(TAG,"onFailure"+e.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "添加课程失败!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                Log.e(TAG,"onResponse " + response.code());
                                String responseJsonString = response.body().string();
                                Log.e(TAG,"onResponse " + responseJsonString);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(MyResponse.getCode(responseJsonString).equals("200")){
                                            Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                            //添加课程后再次调用接口刷新数据
                                            getCourse(courseId,userId);
//                                            flush();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
        );
    }



    public DetailCourse getDetailCourse() {
        return course;
    }

    public void setCourse(DetailCourse detailCourse){
        course = detailCourse;
    }

    //页面刷新
    public void flush() {
        finish();
        Intent intent = new Intent(this, DetailCourseActivity.class);
        startActivity(intent);
    }

    //时间戳转换
    public String DateChange(long time1){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time1 * 1000));
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
                    course = detailCourse1;
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
