package com.example.classMain;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dao.User;
import com.example.picturesharing.R;
import com.example.util.MyResponse;
import com.example.util.MyUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
* 登录界面
* 账号
* 密码
* */
public class LoginActivity extends Activity {

    private User user;
    public static final String TAG = "LoginActivity";
    // 调用Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 关联activity.xml
        setContentView(R.layout.activity_login);
        // 关联用户名、密码和登录、注册按钮
        EditText userName = (EditText) this.findViewById(R.id.UserNameEdit);
        EditText passWord = (EditText) this.findViewById(R.id.PassWordEdit);
        Button loginButton = (Button) this.findViewById(R.id.LoginButton);
        Button signUpButton = (Button) this.findViewById(R.id.SignUpButton);
        // 登录按钮监听器
        loginButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 获取用户名和密码
                        String strUserName = userName.getText().toString().trim();
                        String strPassWord = passWord.getText().toString().trim();
                        if (strUserName.length() > 10) {
                            Toast.makeText(LoginActivity.this, "用户名长度必须小于10！", Toast.LENGTH_SHORT).show();
                        } else if (strUserName.length() < 4) {
                            Toast.makeText(LoginActivity.this, "用户名长度必须大于4！", Toast.LENGTH_SHORT).show();
                        } else if (strPassWord.length() > 16) {
                            Toast.makeText(LoginActivity.this, "密码长度必须小于16！", Toast.LENGTH_SHORT).show();
                        } else if (strPassWord.length() < 6) {
                            Toast.makeText(LoginActivity.this, "密码长度必须大于6！", Toast.LENGTH_SHORT).show();
                        } else {

                            RequestBody requestBody = MyUtil.getBuilder()
                                    .add("username",strUserName)
                                    .add("password",strPassWord)
                                    .build();
                            Request request = MyUtil.post(requestBody,"/sign/user/login");
                            Call call = MyUtil.getCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    Log.e(TAG,"onFailure"+e.toString());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "登录连接失败!", Toast.LENGTH_SHORT).show();
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

                                                Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                                                // 跳转到用户界面
                                                Intent intent = new Intent(LoginActivity.this, BottomBarActivity.class);
                                                intent.putExtra("userDataFromLogin",responseJsonString);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "用户名或者密码错误", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
        );
        // 注册按钮监听器
        signUpButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到注册界面
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

