package com.example.picturesharing;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ExecuteTest {

    @Test
    public void testGet(){
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url("http://www.baidu.com")
                .get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPost(){
        //build返回form表单的请求
        FormBody formBody=new FormBody.Builder().add("a","1").add("b","2").build();

        String json = "{a:1,b:2}";
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        //创建请求对象，url参数为域名
        //post() 默认就是get请求,所以要指明是post请求。post参数：必须传入一个请求体new RequestBody()
        //build返回Request实例对象
        Request request=new Request.Builder().url("https://www.httpbin.org/post").post(formBody).build();
        //得到一个准备好请求的Call对象
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call=okHttpClient.newCall(request);
        try {
            //调用execute进行同步请求
            //得到一个请求响应
            Response response=call.execute();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
