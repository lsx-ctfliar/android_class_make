package com.example.util;

import com.google.gson.Gson;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
public class MyUtil {
//    public static String appId = "9a270a032fc2445680e9034667c17692";
//    public static String appSecret = "286184c554191b545439687b1e03eeecb633e";
    public static String appId = "0209897961f94b7d94d40b6a754a7057";
    public static String appSecret = "586652ad90d5c1de246c58715f1fd1c18ff18";
    public static String appUrl = "http://47.107.52.7:88/member";
    public static int time = 200000;

    public static OkHttpClient getClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(MyUtil.time, TimeUnit.MICROSECONDS)
                .build();
    }

    public static FormBody.Builder getBuilder(){
        return new FormBody.Builder();
    }

//    public static Request getPostRequest(RequestBody requestBody,String url){
//        return new Request.Builder()
//                .url(MyUtil.appUrl + url)
//                .addHeader("Accept", "application/json, text/plain, */*")
//                .addHeader("appId", MyUtil.appId)
//                .addHeader("appSecret", MyUtil.appSecret)
//                .addHeader("Content-Type", "application/json")
//                .post(requestBody)
//                .build();
//    }

    public static Request post(RequestBody requestBody,String url){
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("Content-Type","application/json")
                .add("appId", MyUtil.appId)
                .add("appSecret", MyUtil.appSecret)
                .build();
        return new Request.Builder()
                .url(MyUtil.appUrl + url)
                .headers(headers)
                .post(requestBody)
                .build();
    }

//    public static Call doDelete(String url, String reqbody){
//        Request request = new Request.Builder()
//                .url(url)
//                .delete(RequestBody.create(MEDIA_TYPE_MARKDOWN, reqbody))
//                .build();
//        Call call = client.newCall(request);
//        startrequest(newLogBean(url, "", REQUEST_TYPE_DELECT), call, responseListener, REQUEST_TYPE_DELECT);
//        return call;
//    }

    public static Request delete(Map<String,String> map,String url){
        return new Request.Builder()
                .url(MyUtil.appUrl + MyUtil.getParam(map, url))
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-Type","application/json")
                .addHeader("appId", MyUtil.appId)
                .addHeader("appSecret", MyUtil.appSecret)
                .delete()
                .build();

    }

    public static Request delete(String param){
        return new Request.Builder()
                .url(MyUtil.appUrl + param)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-Type","application/json")
                .addHeader("appId", MyUtil.appId)
                .addHeader("appSecret", MyUtil.appSecret)
                .delete()
                .build();

    }

    public static Request get(Map<String,String> map,String url){
        return new Request.Builder()
                .url(MyUtil.appUrl + MyUtil.getParam(map, url))
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-Type","application/json")
                .addHeader("appId", MyUtil.appId)
                .addHeader("appSecret", MyUtil.appSecret)
                .get()
                .build();

    }
    public static Request get(String param){
        return new Request.Builder()
                .url(MyUtil.appUrl + param)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Content-Type","application/json")
                .addHeader("appId", MyUtil.appId)
                .addHeader("appSecret", MyUtil.appSecret)
                .get()
                .build();

    }

    private static String getParam(Map<String,String> map, String url) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);
        stringBuilder.append("?");
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> next = iterator.next();
            stringBuilder.append(next.getKey());
            stringBuilder.append("=");
            stringBuilder.append(next.getValue());
            if(iterator.hasNext()){
                stringBuilder.append("&");
            }
        }
        return stringBuilder.toString();
    }


    public static Call getCall(Request request){
        return MyUtil.getClient().newCall(request);
    }


    public static String cutJsonFirst(String jsonString){
        System.out.println(jsonString);
        int index1 = jsonString.indexOf("data");
        if(index1 <= 0){
            index1 = 0;
        }
        else {
            index1 += 6;
        }
        int index2 = jsonString.length()-1;
//        int index2 = jsonString.indexOf("}}")+1;
        System.out.println("index1:"+index1);
        System.out.println("index2:"+index2);
        System.out.println("剪切后的json为："+jsonString.substring(index1,index2));
        return jsonString.substring(index1,index2);
    }



    public static <T> T getJsonObject(String jsonString,Class<T> classOfT){
        Gson gson = new Gson();
        String jsonData = MyUtil.cutJsonFirst(jsonString);
        return gson.fromJson(jsonData,classOfT);
    }

    public static String getNowTime(){
        return "";
    }

}
