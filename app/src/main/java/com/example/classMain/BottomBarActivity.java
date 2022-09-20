package com.example.classMain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dao.User;
import com.example.dao.UserTemp;
import com.example.data.ClassData;
import com.example.data.OldTeacherData;
import com.example.data.RecentTeacherData;
import com.example.data.StudentData;
import com.example.picturesharing.R;
import com.example.util.MyResponse;
import com.example.util.MyUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


//底部栏，默认主页
public class BottomBarActivity extends AppCompatActivity {

    public static final String TAG = "BottomBarActivity";

    private BottomNavigationView navigationView;
    //创建三个Fragment和存放他们的数组
    public Fragment[] fragmentlist;
    //首页
    private Fragment headFragment;
    //我的课程
    private Fragment orderFragment;
    //个人中心
    private Fragment userFragment;

    //用于标识上一个fragment
    private int lastFragment;

    private static int courseId;
    private static int courseId2;
    private static int userSignId;
    private static String courseName;
    private static String courseAddr;
    private static String createTime;
    private static User user;
    public String userId;
    public int roleId;
    private String userJsonString;

    //全部课程数据
    public static ClassData classData = new ClassData();
    //教师最近课程数据
    public static RecentTeacherData recentTeacherData = new RecentTeacherData();
    //教师结束课程数据
    private OldTeacherData oldTeacherData = new OldTeacherData();
    //学生加入课程数据
    private StudentData studentData = new StudentData();


    public static int getCourseId() {
        return courseId;
    }
    public static int getCourseId2(){
        return courseId2;
    }
    public static void setCourseId(int courseId) {
        BottomBarActivity.courseId = courseId;
    }
    public static void setCourseId2(int courseId2){BottomBarActivity.courseId2 = courseId2;}
    public static int getUserSignId() {
        return userSignId;
    }
    public static void setUserSignId(int userSignId) {
        BottomBarActivity.userSignId = userSignId;
    }
    public static String getCourseName() {
        return courseName;
    }
    public static void setCourseName(String courseName) {
        BottomBarActivity.courseName = courseName;
    }
    public static String getCourseAddr() {
        return courseAddr;
    }

    public static void setCourseAddr(String courseAddr) {
        BottomBarActivity.courseAddr = courseAddr;
    }

    public static String getCreateTime() {
        return createTime;
    }

    public static void setCreateTime(String createTime) {
        BottomBarActivity.createTime = createTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
        //获取用户数据
        Intent intent = getIntent();
        setUserJsonString(intent.getStringExtra("userDataFromLogin"));
        UserTemp userTemp = MyUtil.getJsonObject(userJsonString,UserTemp.class);
        setUser(userTemp);
        userId = getUser().getId();
        roleId = getUser().getRoleId();

        //获取全部课程
        setClassData(1,100,classData);
        //身份为教师
        if(roleId==1){
            getTeacherData(1,10,recentTeacherData,oldTeacherData);
        }
        else {
            getStudentData(1,10,studentData);
        }

        //导航栏
        setContentView(R.layout.activity_bottom_bar);
        //fragment初始化
        initFragment();
    }

    /**
     * 初始化fragment，并将headFragment显示出来
     */
    private void initFragment() {
        navigationView = (BottomNavigationView) findViewById(R.id.bnv_main);
        //配置菜单按钮显示图标
        navigationView.setItemIconTintList(null);
        //将三个fragment先放在数组里
        //接口用完了，换一个试试
        headFragment = new HomePageActivity();
        if(roleId == 1){
            //老师
            orderFragment = new TeacherClassActivity();
        }
        else {
            orderFragment = new StudentClassActivity();
        }
        userFragment = new UserActivity();
        fragmentlist = new Fragment[]{headFragment, orderFragment, userFragment};
        //此时标识标识首页
        //0表示首页，1表示orderFragment，2表示userFragment
        lastFragment = 0;
        //为navigationView设置点击事件
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //设置默认页面为headFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, headFragment)
                .show(headFragment).commit();
        navigationView.setSelectedItemId(R.id.navigation_home);
    }

    /**
     * 给BottomNavigationView添加按钮的点击事件
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //每次点击后都将所有图标重置到默认不选中图片
            resetToDefaultIcon();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //判断要跳转的页面是否是当前页面，若是则不做动作
                    if (lastFragment != 0) {
                        switchFragment(lastFragment, 0);
                        lastFragment = 0;
                    }
                    //设置按钮的
                    item.setIcon(R.drawable.home);
                    return true;
                case R.id.navigation_buy:
                    if (lastFragment != 1) {
                        switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                    item.setIcon(R.drawable.classlist);
                    return true;
                case R.id.navigation_user:
                    if (lastFragment != 2) {
                        switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                    item.setIcon(R.drawable.persion);
                    return true;
            }
            return false;
        }
    };

    /**
     *
     * @param lastFragment 表示点击按钮前的页面
     * @param index 表示点击按钮对应的页面
     */
    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragmentlist[lastFragment]);

        //判断transaction中是否加载过index对应的页面，若没加载过则加载
        if (fragmentlist[index].isAdded() == false) {
            transaction.add(R.id.fl_main, fragmentlist[index]);
        }
        //根据角标将fragment显示出来
        transaction.show(fragmentlist[index]).commitAllowingStateLoss();
    }


    /**
     * 重新配置每个按钮的图标
     */
    private void resetToDefaultIcon() {
        navigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_gray);
        navigationView.getMenu().findItem(R.id.navigation_buy).setIcon(R.drawable.classlist_gray);
        navigationView.getMenu().findItem(R.id.navigation_user).setIcon(R.drawable.persion_gray);
    }

    public static User getUser() {
        return user;
    }

    public void setUser(){
        this.user = (User) getApplication();
    }
    public void setUser(UserTemp userTemp) {
        this.user = (User) getApplication();
        this.user.setId(userTemp.getId());
        this.user.setAppKey(userTemp.getAppKey());
        this.user.setUserName(userTemp.getUserName());
        this.user.setRoleId(userTemp.getRoleId());
        this.user.setRealName(userTemp.getRealName());
        this.user.setIdNumber(userTemp.getIdNumber());
        this.user.setCollegeName(userTemp.getCollegeName());
        this.user.setGender(userTemp.isGender());
        this.user.setPhone(userTemp.getPhone());
        this.user.setEmail(userTemp.getEmail());
        this.user.setAvatar(userTemp.getAvatar());
        this.user.setInSchoolTime(userTemp.getInSchoolTime());
        this.user.setCreateTime(userTemp.getCreateTime());
        this.user.setLastUpdateTime(userTemp.getLastUpdateTime());
    }

    public String getUserJsonString() {
        return userJsonString;
    }

    public void setUserJsonString(String userJsonString) {
        this.userJsonString = userJsonString;
    }

    @Override       //这里是实现了自动更新
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setUser();
        initFragment();
    }
    public ClassData getClassData() {
        return classData;
    }

    public void setClassData(ClassData classData) {
        this.classData = classData;
    }

    //通过接口获得所有课程数据
    public void setClassData(int current, int size,ClassData classData){
        String param = "/sign/course/all?current="+current+"&size="+size;
        Request request = MyUtil.get(param);
        Call call = MyUtil.getCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"onFailure"+e.toString());
                // todo 没有课程就给一个提示数据

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络问题，课程数据加载!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e(TAG,"onResponse " + response.code());
                String responseJsonString = response.body().string();
                Log.e(TAG,"onResponse " + responseJsonString);
                if(MyResponse.getCode(responseJsonString).equals("200")){
                    ClassData classData1 = MyUtil.getJsonObject(responseJsonString,ClassData.class);
                    Log.e(TAG,"classData1.string " + classData1.toString());
                    classData.setCurrent(classData1.getCurrent());
                    classData.setSize(classData1.getSize());
                    classData.setTotal(classData1.getTotal());
                    classData.setRecords(classData1.getRecords());
                    Log.e(TAG,"classData.string " + classData.toString());
                }
                else {
                    recentTeacherData.setSize(10);
                }
            }
        });
    }

    //通过接口获得教师的课程
    public void getTeacherData(int current,int size,RecentTeacherData recentTeacherData,OldTeacherData oldTeacherData){
        //未结课的
        String param = "/sign/course/teacher/unfinished?current="+current+"&size="+size+"&userId="+userId;
        Request request = MyUtil.get(param);
        System.out.println(request.url());
        Call call = MyUtil.getCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"onFailure"+e.toString());
                // todo 没有课程就给一个提示数据

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络问题，课程数据加载!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody =response.body();
                String responseJsonString = responseBody.string();
                Log.e(TAG,"getTeacherData recent onResponse " + response.code());
                Log.e(TAG,"getTeacherData recent onResponse " + responseJsonString);
                if(MyResponse.getCode(responseJsonString).equals("200")){
                    RecentTeacherData recentTeacherDataTemp = MyUtil.getJsonObject(responseJsonString,RecentTeacherData.class);
                    recentTeacherData.setRecords(recentTeacherDataTemp.getRecords());
                    recentTeacherData.setSize(recentTeacherDataTemp.getSize());
                    recentTeacherData.setTotal(recentTeacherDataTemp.getTotal());
                    recentTeacherData.setCurrent(recentTeacherDataTemp.getCurrent());
                }
                else {
                    recentTeacherData.setSize(10);
                }
            }
        });

//        //已经结课的
        param = "/sign/course/teacher/finished?current="+current+"&size="+size+"&userId="+userId;
        request = MyUtil.get(param);
        Call call2 = MyUtil.getCall(request);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"onFailure"+e.toString());
                // todo 没有课程就给一个提示数据

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络问题，课程数据加载!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody =response.body();
                String responseJsonString = responseBody.string();
                Log.e(TAG,"onResponse " + response.code());
                Log.e(TAG,"onResponse " + responseJsonString);
                if(MyResponse.getCode(responseJsonString).equals("200")){
                    OldTeacherData oldTeacherDataTemp = MyUtil.getJsonObject(responseJsonString,OldTeacherData.class);
                    oldTeacherData.setRecords(oldTeacherDataTemp.getRecords());
                    oldTeacherData.setSize(oldTeacherDataTemp.getSize());
                    oldTeacherData.setTotal(oldTeacherDataTemp.getTotal());
                    oldTeacherData.setCurrent(oldTeacherDataTemp.getCurrent());
                }
                else {
                    oldTeacherData.setSize(10);
                }
            }
        });
    }

    // 获取学生课程
    public void getStudentData(int current,int size,StudentData studentData){
        //未结课的
        String param = "/sign/course/student?current="+current+"&size="+size+"&userId="+userId;
        Request request = MyUtil.get(param);
        Log.e(TAG,"getStudentData " +request);
        Call call = MyUtil.getCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG,"onFailure"+e.toString());
                // todo 没有课程就给一个提示数据

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "网络问题，课程数据加载!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody =response.body();
                String responseJsonString = responseBody.string();
                Log.e(TAG,"getStudentData recent onResponse " + response.code());
                Log.e(TAG,"getStudentData recent onResponse " + responseJsonString);
                if(MyResponse.getCode(responseJsonString).equals("200")){
                    StudentData studentDataTemp = MyUtil.getJsonObject(responseJsonString,StudentData.class);
                    studentData.setRecords(studentDataTemp.getRecords());
                    studentData.setSize(studentDataTemp.getSize());
                    studentData.setTotal(studentDataTemp.getTotal());
                    studentData.setCurrent(studentDataTemp.getCurrent());
                }
                else {
                    studentData.setSize(10);
                }
            }
        });
    }



    public RecentTeacherData getRecentTeacherData() {
        return recentTeacherData;
    }

    public OldTeacherData getOldTeacherData() {
        return oldTeacherData;
    }

    public StudentData getStudentData() {
        return studentData;
    }

    public void setStudentData(StudentData studentData) {
        this.studentData = studentData;
    }
}