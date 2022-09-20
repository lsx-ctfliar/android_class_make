package com.example.picturesharing;

import com.example.util.MyUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiTest {
    OkHttpClient clinet = new OkHttpClient.Builder()
            .connectTimeout(8000, TimeUnit.MICROSECONDS)
            .build();

    FormBody.Builder builder = new FormBody.Builder();



    RequestBody requestBody = builder
            .add("username","1321")
            .add("password","1323")
            .build();
    Request request = new Request.Builder()
            .url(MyUtil.appUrl+"/as")
            .addHeader("Accept","application/json, text/plain, */*")
            .addHeader("appId", "用户所申请的应用ID")
            .addHeader("appSecret", "用户所申请的应用密钥")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build();





}