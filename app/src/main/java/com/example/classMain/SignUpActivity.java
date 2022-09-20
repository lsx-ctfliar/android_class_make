package com.example.classMain;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dao.Register;
import com.example.picturesharing.R;
import com.example.util.MyPost;
import com.example.util.MyResponse;
import com.example.util.MyUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/*
*注册界面
* 用户名
* 密码
* 再次密码
 */
public class SignUpActivity extends Activity {
    private static final String TAG = "SignUpActivity";
    private String role;

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    // 调用Actvity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //关联activity_register.xml
        setContentView(R.layout.activity_sign_up);
        // 关联用户名、密码、确认密码、邮箱和注册、返回登录按钮
        EditText userName = (EditText) this.findViewById(R.id.UserNameEdit);
        EditText passWord = (EditText) this.findViewById(R.id.PassWordEdit);
        EditText passWordAgain = (EditText) this.findViewById(R.id.PassWordAgainEdit);
        //角色选择
        RadioGroup radioGroup = this.findViewById(R.id.RoleChoose);
        setRole("1");
        //注册
        Button signUpButton = (Button) this.findViewById(R.id.SignUpButton);
        //登录
        Button backLoginButton = (Button) this.findViewById(R.id.BackLoginButton);
        //角色选择改变
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radbtn = (RadioButton) findViewById(i);
                if(radbtn.getText().equals("申请为教师")){
                    setRole("1");
                }
                else {
                    setRole("0");
                }
            }
        });

        // 立即注册按钮监听器
        signUpButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strUserName = userName.getText().toString().trim();
                        String strPassWord = passWord.getText().toString().trim();
                        String strPassWordAgain = passWordAgain.getText().toString().trim();
                        //注册格式粗检
                        if (strUserName.length() > 10) {
                            Toast.makeText(SignUpActivity.this, "用户名长度必须小于10！", Toast.LENGTH_SHORT).show();
                        } else if (strUserName.length() < 4) {
                            Toast.makeText(SignUpActivity.this, "用户名长度必须大于4！", Toast.LENGTH_SHORT).show();
                        } else if (strPassWord.length() > 16) {
                            Toast.makeText(SignUpActivity.this, "密码长度必须小于16！", Toast.LENGTH_SHORT).show();
                        } else if (strPassWord.length() < 6) {
                            Toast.makeText(SignUpActivity.this, "密码长度必须大于6！", Toast.LENGTH_SHORT).show();
                        } else if (!strPassWord.equals(strPassWordAgain)) {
                            Toast.makeText(SignUpActivity.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
                        } else {
                            Register register = new Register();
                            register.setUsername(strUserName);
                            register.setPassword(strPassWord);
                            register.setRole(getRole());
                            Request request = MyPost.post(register,"/sign/user/register");
                            System.out.println(request);
                            Call call = MyUtil.getCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    Log.e(TAG,"onFailure"+e.toString());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "注册连接失败!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                //成功
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    Log.e(TAG,"onResponse " + response.code());
                                    String body = response.body().string();
                                    Log.e(TAG,"onResponse: " + body);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(MyResponse.getCode(body).equals("200"))
                                            {
                                                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                                // 跳转到登录界面
                                                finish();
                                            }
                                            else if(MyResponse.isUserExist(body)){
                                                Toast.makeText(getApplicationContext(), "用户已存在", Toast.LENGTH_SHORT).show();
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
                }
        );


        // 返回登录按钮监听器
        backLoginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 跳转到登录界面
                        finish();
                    }
                }
        );

    }
}