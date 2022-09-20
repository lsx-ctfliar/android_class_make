package com.example.picturesharing;


import com.example.util.MyDateUtil;

import org.junit.Test;

public class DateTest {
    @Test
    public void testStringToLong(){
        String time = "2021年09月7日 12:00:00";
        long ans = MyDateUtil.stringToLong(time);
        System.out.println(ans);
    }

    @Test
    public void testLongToString(){
        long time = 122354678900L;
        String ans = MyDateUtil.longToString(time);
        System.out.println(ans);
    }

}
