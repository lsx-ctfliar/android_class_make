package com.example.picturesharing;

import com.example.dao.User;
import com.example.util.MyUtil;
import com.google.gson.Gson;

import org.junit.Test;


public class GsonTest {


    @Test
    public void testJosnToClass(){
        String json = "{\"id\":\"102\",\"appKey\":\"97b903276b31414ba827016e4fe3a17f\",\"userName\":\"chenlitao\",\"roleId\":0,\"realName\":null,\"idNumber\":null,\"collegeName\":null,\"gender\":true,\"phone\":null,\"email\":null,\"avatar\":null,\"inSchoolTime\":null,\"createTime\":\"1662280683510\",\"lastUpdateTime\":\"1662280683510\"}";
        String json2 = "{\"code\":200,\"msg\":\"登录成功\",\"data\":{\"id\":\"102\",\"appKey\":\"97b903276b31414ba827016e4fe3a17f\",\"userName\":\"chenlitao\",\"roleId\":0,\"realName\":null,\"idNumber\":null,\"collegeName\":null,\"gender\":true,\"phone\":null,\"email\":null,\"avatar\":null,\"inSchoolTime\":null,\"createTime\":\"1662280683510\",\"lastUpdateTime\":\"1662280683510\"}}";
        Gson gson = new Gson();
        User user = gson.fromJson(MyUtil.cutJsonFirst(json2),User.class);
        System.out.println(user.toString());
    }
}
