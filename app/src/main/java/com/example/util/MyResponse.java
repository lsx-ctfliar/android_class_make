package com.example.util;

import android.util.Log;

public class MyResponse {
     public static final String TAG = "MyResponse";

     public static String getCode(String response) {
          int index = response.indexOf("code") + 6;
          int index2 = response.indexOf(",\"msg\"");
          if(index <= 6){
               return "501";
          }
          String code = response.substring(index,index2);
          Log.e(TAG,"--" + code + "--");
          return code;
     }
     public static boolean isUserExist(String response) {
          if(response.indexOf("用户名已存在")>0){
               return true;
          }
          return false;
     }

}
