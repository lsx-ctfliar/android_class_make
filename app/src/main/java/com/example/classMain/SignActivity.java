package com.example.classMain;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adapter.SignAdapter;
import com.example.dao.User;
import com.example.data.ClassQiandao;
import com.example.picturesharing.R;
import com.example.util.MyResponse;
import com.example.util.MyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SignActivity extends AppCompatActivity {

    public static final String TAG = "SignActivity";
    private static int courseId;
    private static User user;
    public int userId;
    public long amount=0;
    private String userJsonString;
    private ListView listView;
    //签到列表
    ClassQiandao classQiandao = new ClassQiandao();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取用户数据
        getClassQiandao(1,10,1,classQiandao);
        classQiandao = getClassQiandao();

        //代替循坏
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //有数据
        if(classQiandao.getRecords()!=null){
            setContentView(R.layout.qiandao_record);
            List<Map<String, Object>> classQiandaoList = getClassQiandaoList(classQiandao);
            SignAdapter signAdapter = new SignAdapter(this,classQiandaoList);
            listView = findViewById(R.id.qiandao_list);
            listView.setAdapter(signAdapter);
        //无数据
        }else{
            Toast.makeText(getApplicationContext(), "无签到记录,请返回", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.error_qiandao_record);
        }

    }

    public ClassQiandao getClassQiandao() {
        return classQiandao;
    }

    //todo 签到列表数据
    public void getClassQiandao(int current, int size, int status, ClassQiandao classQiandao){
        courseId = BottomBarActivity.getCourseId2();
        userId = Integer.parseInt(BottomBarActivity.getUser().getId());
//        courseId=60;
//        userId=101;

        System.out.println("拿取到的courseId的数据为=>"+courseId);
        System.out.println("拿取到的userId的数据为=>"+userId);

        String NoSign = "/sign/course/student/signList?courseId="+courseId+"&current="+current+"&size="+size+"&status="+0+"&userId="+userId;
        String HasSign = "/sign/course/student/signList?courseId="+courseId+"&current="+current+"&size="+size+"&status="+1+"&userId="+userId;
        Request request1 = MyUtil.get(NoSign);   //get未签到请求
        Request request2 = MyUtil.get(HasSign); //get已签到请求
        Call call1 = MyUtil.getCall(request1);  //未签到
        Call call2 = MyUtil.getCall(request2);  //已签到
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"onFailure"+e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络问题，课程列表正在加载!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseJsonString = responseBody.string();
                if(MyResponse.getCode(responseJsonString).equals("200")){
                    ClassQiandao QiandaoDataTemp = MyUtil.getJsonObject(responseJsonString,ClassQiandao.class);
                    if(QiandaoDataTemp==null){
                        System.out.println("本课程暂无未签到记录");
                    }
                    else{
                        classQiandao.setRecords(QiandaoDataTemp.getRecords());
                        classQiandao.setSize(QiandaoDataTemp.getSize());
                        classQiandao.setTotal(QiandaoDataTemp.getTotal());
                        classQiandao.setCurrent(QiandaoDataTemp.getCurrent());
                    }

                }
            }
        }
        );
        call2.enqueue(new Callback() {
                          @Override
                          public void onFailure(@NonNull Call call, @NonNull IOException e) {
                              Log.e(TAG,"onFailure"+e.toString());
                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      Toast.makeText(getApplicationContext(), "网络问题，课程列表正在加载!", Toast.LENGTH_SHORT).show();
                                  }
                              });
                          }

                          @Override
                          public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                              ResponseBody responseBody = response.body();
                              String responseJsonString = responseBody.string();
                              if(MyResponse.getCode(responseJsonString).equals("200")){
                                  ClassQiandao QiandaoDataTemp = MyUtil.getJsonObject(responseJsonString,ClassQiandao.class);
                                  if(QiandaoDataTemp==null){
                                      System.out.println("本课程暂无已签到记录");
                                  }
                                  else{
                                      classQiandao.setRecords(QiandaoDataTemp.getRecords());
                                      classQiandao.setSize(QiandaoDataTemp.getSize());
                                      classQiandao.setTotal(QiandaoDataTemp.getTotal());
                                      classQiandao.setCurrent(QiandaoDataTemp.getCurrent());
                                  }

                              }
                          }
                      }
        );
    }

    public List<Map<String, Object>> getClassQiandaoList(ClassQiandao classQiandao){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < (classQiandao.getRecords()).size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("userSignId",classQiandao.getRecords().get(i).getUserSignId());
            map.put("courseName",classQiandao.getRecords().get(i).getCourseName());
            map.put("courseAddr",classQiandao.getRecords().get(i).getCourseAddr());
            map.put("SignTime",classQiandao.getRecords().get(i).getCreateTime());
            list.add(map);
        }
        return list;
    }

    public static int getCourseId() {
        return courseId;
    }

    public static void setCourseId(int courseId) {
        SignActivity.courseId = courseId;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        SignActivity.user = user;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserJsonString() {
        return userJsonString;
    }

    public void setUserJsonString(String userJsonString) {
        this.userJsonString = userJsonString;
    }

    public void setClassQiandao(ClassQiandao classQiandao) {
        this.classQiandao = classQiandao;
    }
}
