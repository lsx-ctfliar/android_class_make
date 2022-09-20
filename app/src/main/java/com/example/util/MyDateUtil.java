package com.example.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateUtil {
    public static long stringToLong(String time){
        time = time.replace("年","-");
        time = time.replace("月","-");
        time = time.replace("日","");
        if( time.indexOf(":")==-1 ){
            time = time+" 0:0:0";
        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
            System.out.println("date"+date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ans = date.getTime();
        return ans;
    }
    public static String longToString(long time){
        String ans = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time * 1000));
        ans=ans.replace("-","年");
        ans.replace("-","月");
        ans.replace("-","日");
        return ans;
    }
}
