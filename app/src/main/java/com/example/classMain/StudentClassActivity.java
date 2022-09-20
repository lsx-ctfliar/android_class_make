package com.example.classMain;

import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.adapter.StudentCoursesAdapter;
import com.example.adapter.StudentCoursesAdapter.InnerItemOnclickListener;
import com.example.data.StudentData;
import com.example.picturesharing.R;
import com.example.util.MyResponse;
import com.example.util.MyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StudentClassActivity extends Fragment implements InnerItemOnclickListener {
    private static final String TAG = "StudentClassActivity";
    private ListView studentClassList;
    private StudentCoursesAdapter mAdapter;
    List<Map<String,Object>> studentList;
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    studentList.clear();
                    studentList.addAll((Collection<? extends Map<String, Object>>) msg.obj) ;
                    Log.e("two:",String.valueOf(studentList));
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    int position = (int) msg.obj;
                    studentList.remove(position);
                    mAdapter.notifyDataSetInvalidated();
                    break;
            }
        }
    };
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity(); //获取主活动中的对象
            String param1 = "/sign/course/student?userId="+bottomBarActivity.userId;
            //todo 学生数据同步请求
            Request request1 = MyUtil.get(param1);
            Log.e("Request", String.valueOf(request1));
            Call call1 = MyUtil.getCall(request1);
            call1.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    //todo
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    ResponseBody responseBody =response.body();
                    String responseJsonString = responseBody.string();
                    Log.e(TAG,"getStudentData recent onResponse " + response.code());
                    Log.e(TAG,"getStudentData recent onResponse " + responseJsonString);
                    if(MyResponse.getCode(responseJsonString).equals("200")){
                        StudentData studentDataTemp = MyUtil.getJsonObject(responseJsonString,StudentData.class);
                        BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity(); //获取主活动中的对象
                        //修改主活动中的数据
                        bottomBarActivity.setStudentData(studentDataTemp);
                        Log.e("error1",studentDataTemp.toString());
                        List<Map<String,Object>> studentList1;
                        studentList1 = getStudentList(studentDataTemp);

                        Log.e("one:",String.valueOf(studentList1));

                        Message msg = Message.obtain();
                        msg.obj = studentList1;
                        msg.what = 1;
                        mhandler.sendMessage(msg);
                    }
                    else {
                    }
                }
            });
//            BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity(); //获取主活动中的对象
//            String param1 = "/sign/course/student?userId="+bottomBarActivity.userId;
//            //todo 学生数据同步请求
//            Request request1 = MyUtil.get(param1);
//            Log.e("Request", String.valueOf(request1));
//            Call call1 = MyUtil.getCall(request1);
//            Response response = null;
//            try {
//                response = call1.execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            ResponseBody responseBody =response.body();
//            String responseJsonString = responseBody.toString();
//            StudentData studentDataTemp = MyUtil.getJsonObject(responseJsonString,StudentData.class);
//            //修改主活动中的数据
//            bottomBarActivity.setStudentData(studentDataTemp);
//            studentList = getStudentList(studentDataTemp);
//            mAdapter.notifyDataSetChanged();
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity(); //获取主活动中的对象
        View view = inflater.inflate(R.layout.activity_students_course,container,false);
        studentClassList = view.findViewById(R.id.students_courses);
        StudentData studentData = bottomBarActivity.getStudentData();
        while (studentData.getSize()==0){
            studentData = bottomBarActivity.getStudentData();
            System.out.println("---------------"+studentData.toString());
        }

        if (studentData.getRecords()!=null){
            studentList = getStudentList(studentData);
            mAdapter = new StudentCoursesAdapter(getContext(),studentList);
            mAdapter.setOnInnerItemOnClickListener(this);
            studentClassList.setAdapter(mAdapter);
        }
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe3);

//设置进度条的颜色，不定长参数可以设置多种颜色
//对于RefreshLayout，网上有人说最多4种颜色，不要使用android.R.color.，否则会卡死
        refreshLayout.setColorSchemeColors(
                Color.RED,
                Color.YELLOW,
                Color.GREEN);

//设置进度条的背景颜色
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
//设置大小
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);
//设置手指划过多少像素开始触发刷新
        refreshLayout.setDistanceToTriggerSync(100);
//设置刷新的时候监听，三秒钟之后添加数据完毕
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //模拟网络请求数据
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //recyclerView回到最上面
                        studentClassList.scrollListBy(0);
                        String param = "/sign/course/student?userId="+bottomBarActivity.userId;
                        //判断是否在刷新
                        //todo 学生数据同步请求
                        Request request1 = MyUtil.get(param);
                        Log.e("Request", String.valueOf(request1));
                        Call call1 = MyUtil.getCall(request1);
                        call1.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                //todo
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                ResponseBody responseBody =response.body();
                                String responseJsonString = responseBody.string();
                                Log.e(TAG,"getStudentData recent onResponse " + response.code());
                                Log.e(TAG,"getStudentData recent onResponse " + responseJsonString);
                                if(MyResponse.getCode(responseJsonString).equals("200")){
                                    StudentData studentDataTemp = MyUtil.getJsonObject(responseJsonString,StudentData.class);
                                    BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity(); //获取主活动中的对象
                                    bottomBarActivity.setStudentData(studentDataTemp);
                                    studentList = getStudentList(studentDataTemp);
                                }
                                else {

                                }
                            }
                        });
//                        mAdapter.notifyDataSetChanged();
//                        mAdapter = new StudentCoursesAdapter(getContext(),studentList);
//                        studentClassList.setAdapter(mAdapter);
//                        studentClassList.setAdapter(mAdapter);
//                      refreshLayout.isRefreshing()
                        //刷新完毕，关闭下拉刷新的组件
                        refreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        if(v.getId() == R.id.delete_class_student)
        {
            Log.e("delete class:",position+"");
            BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity(); //获取主活动中的对象
            StudentData studentData = bottomBarActivity.getStudentData();
            studentList = getStudentList(studentData);
            Log.e("studemtData:",(String) studentList.get(position).get("courseId")+"   "+bottomBarActivity.userId);
            String param = "/sign/course/student/drop?courseId="+(String) studentList.get(position).get("courseId")+"&userId="+bottomBarActivity.userId;

            Request request = MyUtil.delete(param);
            Log.e(TAG,"delete request "+ request);
            Call call = MyUtil.getCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "网络问题，课程数据加载!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    System.out.println("删除成功！！！！！！！！！！！！！！！！");
                    ResponseBody responseBody =response.body();
                    String responseJsonString = responseBody.string();
                    Log.e(TAG,"delete class onResponse " + response.code());
                    Log.e(TAG,"delete class onResponse " + responseJsonString);

                    Message msg = Message.obtain();
                    msg.obj = position;
                    msg.what = 2;
                    mhandler.sendMessage(msg);
                }
            });

            //删除成功后，去查询并且修改数据
            String param1 = "/sign/course/student?userId="+bottomBarActivity.userId;
            //todo 学生数据同步请求
            Request request1 = MyUtil.get(param1);
            Log.e("Request", String.valueOf(request1));
            Call call1 = MyUtil.getCall(request1);
            call1.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    //todo
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    ResponseBody responseBody =response.body();
                    String responseJsonString = responseBody.string();
                    Log.e(TAG,"getStudentData recent onResponse " + response.code());
                    Log.e(TAG,"getStudentData recent onResponse " + responseJsonString);
                    if(MyResponse.getCode(responseJsonString).equals("200")){
                        StudentData studentDataTemp = MyUtil.getJsonObject(responseJsonString,StudentData.class);
                        BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity(); //获取主活动中的对象
                        //修改主活动中的数据
                        bottomBarActivity.setStudentData(studentDataTemp);
                        Log.e("error1",studentDataTemp.toString());
                        studentList = getStudentList(studentDataTemp);
                    }
                    else {
                    }
                }
            });
            mAdapter = new StudentCoursesAdapter(getContext(),studentList);
            mAdapter.setOnInnerItemOnClickListener(this);
            studentClassList.setAdapter(mAdapter);
        }
    }
    public List<Map<String,Object>> getStudentList(StudentData studentData){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < (studentData.getRecords()).size(); i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("coursePhoto",studentData.getRecords().get(i).getCoursePhoto());
            map.put("courseName", studentData.getRecords().get(i).getCourseName());
            map.put("collegeName", studentData.getRecords().get(i).getCollegeName());
            map.put("courseId", studentData.getRecords().get(i).getCourseId());
            list.add(map);
        }
        return list;
    }
}
