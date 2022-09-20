package com.example.classMain;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dao.UpdateUser;
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

public class UpdateUserActivity extends AppCompatActivity {
    private static final String TAG = "UpdateUserActivity";

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUser();

        setContentView(R.layout.activity_update_user);

        ImageView back = (ImageView) findViewById(R.id.iv_back);
        ImageView tv_avatar = (ImageView) findViewById(R.id.tv_avatar);
        EditText tv_userName = (EditText) findViewById(R.id.tv_userName);
        EditText tv_collegeName = (EditText) findViewById(R.id.tv_collegeName);
        EditText tv_realName = (EditText) findViewById(R.id.tv_realName);
        EditText tv_idNumber = (EditText) findViewById(R.id.tv_idNumber);
        EditText tv_gender = (EditText) findViewById(R.id.tv_gender);
        EditText tv_phone = (EditText) findViewById(R.id.tv_phone);
        EditText tv_email = (EditText) findViewById(R.id.tv_email);
        EditText tv_inSchoolTime = (EditText) findViewById(R.id.tv_inSchoolTime);
        Button save = (Button) findViewById(R.id.save);

        //退出
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //头像
        //用户名
        tv_userName.setText(getUser().getUserName());
        //所在学校
        tv_collegeName.setText(getUser().getCollegeName());
        //真实姓名
        tv_realName.setText(getUser().getRealName());
        //学/工号
        tv_idNumber.setText(getUser().getIdNumber());
        //性别
        boolean gender = getUser().isGender();
        if(!gender){
            tv_gender.setText("女");
        }
        else {
            tv_gender.setText("男");
        }
        //电话
        tv_phone.setText(getUser().getPhone());
        //邮箱
        tv_email.setText(getUser().getEmail());
        //注册时间
        tv_inSchoolTime.setText(getUser().getInSchoolTime());
        //保存
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUser updateUser = new UpdateUser();
                updateUser.setId(Integer.parseInt(user.getId()));
                updateUser.setCollegeName(String.valueOf(tv_collegeName.getText()));
                updateUser.setEmail(String.valueOf(tv_email.getText()));
                if(String.valueOf(tv_gender.getText()).equals("男")){
                    updateUser.setGender(true);
                }
                else {
                    updateUser.setGender(false);
                }
                updateUser.setIdNumber(String.valueOf(tv_idNumber.getText()));
                //todo 在校时间
                updateUser.setInSchoolTime(String.valueOf(tv_inSchoolTime.getText()));
                updateUser.setPhone(String.valueOf(tv_phone.getText()));
                updateUser.setRealName(String.valueOf(tv_realName.getText()));
                updateUser.setUserName(String.valueOf(tv_userName.getText()));

                Request request = MyPost.post(updateUser,"/sign/user/update");
                Call call = MyUtil.getCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(TAG,"onFailure: "+e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "网络出错", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    //成功
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.e(TAG,"onResponse: " + response.code());
                        String body = response.body().string();
                        Log.e(TAG,"onResponse: " + body);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(MyResponse.getCode(body).equals("200"))
                                {
                                    Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                    setUser(updateUser);
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "服务器错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

    }

    public User getUser() {
        return user;
    }

    public void setUser() {
        this.user = (User) getApplication();
    }

    public void setUser(UpdateUser updateUser) {
        this.user.setId(updateUser.getId()+"");
        this.user.setUserName(updateUser.getUserName());
        this.user.setRealName(updateUser.getRealName());
        this.user.setIdNumber(updateUser.getIdNumber());
        this.user.setCollegeName(updateUser.getCollegeName());
        this.user.setGender(updateUser.isGender());
        this.user.setPhone(updateUser.getPhone());
        this.user.setEmail(updateUser.getEmail());
        this.user.setAvatar(updateUser.getAvatar());
        this.user.setInSchoolTime(updateUser.getInSchoolTime());
    }
}