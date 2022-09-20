package com.example.classMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dao.Qiandao;
import com.example.dao.User;
import com.example.picturesharing.R;
import com.example.util.MyPost;
import com.example.util.MyResponse;
import com.example.util.MyUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class QiandaoActivity extends Activity {
    private User user;
    public static final String TAG = "QiandaoActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //关联界面
        setContentView(R.layout.user_qiandao);
        //关联返回键
        Button retu = (Button) this.findViewById(R.id.qiandao_retu);
        //关联签到码输入
        EditText SignCode = (EditText) this.findViewById(R.id.SignCode);
        //关联签到按钮
        Button SignButton = (Button) this.findViewById(R.id.qindao_button);
        //关联课程信息
        TextView courseName = (TextView) this.findViewById(R.id.course_name);
        TextView userSignId = (TextView) this.findViewById(R.id.course_id);
        TextView courseAddr = (TextView) this.findViewById(R.id.course_addr);

        courseName.setText(BottomBarActivity.getCourseName());
        userSignId.setText(Integer.toString(BottomBarActivity.getUserSignId()));
        courseAddr.setText(BottomBarActivity.getCourseAddr());

        //签到按钮监听器
        SignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入的签到码
                int signCode = Integer.parseInt(SignCode.getText().toString().trim());
                Qiandao user1 = new Qiandao();

                user1.setUserId(Integer.parseInt(BottomBarActivity.getUser().getId()));
                user1.setUserSignId(BottomBarActivity.getUserSignId());
//                user1.setUserId(101);
//                user1.setUserSignId(50);

                user1.setSignCode(signCode);
                Request request = MyPost.post(user1,"/sign/course/student/sign");

                Call call = MyUtil.getCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(TAG,"onFailure"+e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "连接失败!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    //成功
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.e(TAG,"onResponse " + response.code());
                        String body = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(MyResponse.getCode(body).equals("200"))
                                {
                                    Toast.makeText(getApplicationContext(), "签到成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else if(MyResponse.getCode(body).equals("500")){
                                    Toast.makeText(getApplicationContext(), "签到失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            }
        });

        retu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(QiandaoActivity.this,SignActivity.class);
                startActivity(intent);
            }
        });



    }

}
