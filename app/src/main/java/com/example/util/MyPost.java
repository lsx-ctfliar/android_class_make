package com.example.util;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyPost {
    public static Request post(Object T, String url){
        Gson gson = new Gson();
        String json = gson.toJson(T);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return MyUtil.post(requestBody,url);
    }


    public static Request postFile(File file, String url){
        // url路径
        url = "http://47.107.52.7:88/member"+url;
        // 请求头
        Headers headers = new Headers.Builder()
                .add("appId", MyUtil.appId)
                .add("appSecret", MyUtil.appSecret)
                .add("Accept", "application/json, text/plain, */*")
                .build();
        MediaType MEDIA_TYPE_PNG = MediaType.parse("application/image/png; charset=utf-8");
        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("file",file.getName(), RequestBody.create(MEDIA_TYPE_PNG,file));
        RequestBody requestBody =mbody.build();
        //请求组合创建
        Request request = new Request.Builder()
                .url(url)
                // 将请求头加至请求中
                .headers(headers)
                .post(requestBody)
                .build();
        return request;
    }


    public static void postZZ(ArrayList<File> files){
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/sign/image/upload";
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", MyUtil.appId)
                    .add("appSecret", MyUtil.appSecret)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();


            MediaType MEDIA_TYPE_PNG = MediaType.parse("application/image/jpg; charset=utf-8");
            //Todopng修改
            MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);

            for(File file:files){
                if(file.exists()){
                    Log.i("asd",file.getName());//经过测试，此处的名称不能相同，如果相同，只能保存最后一个图片，不知道那些同名的大神是怎么成功保存图片的。
                    mbody.addFormDataPart("file",file.getName(), RequestBody.create(MEDIA_TYPE_PNG,file));
                }
            }


            RequestBody requestBody =mbody.build();
            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(requestBody)
                    .build();

            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("TAG", "onFailure: ");
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String da = response.body().string();
                        Log.e("astt",da);

                    }
                });
                //请求完成一次之后就给list置空，避免图片的叠加提交
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }
}
