package com.example.adapter;

import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dao.DetailCourse;
import com.example.classMain.BottomBarActivity;
import com.example.classMain.DetailCourseActivity;
import com.example.picturesharing.R;
import com.example.util.MyResponse;
import com.example.util.MyUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class CourseAdapter extends BaseAdapter {

    public List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    public static final String TAG = "DetailCourseActivity";

    public CourseAdapter(Context context,List<Map<String,Object>> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    /**
     * 组件集合，对应list.xml中的控件
     * @author Administrator
     */

    public final class Course {
        TextView courseName;
        TextView courseId;
        TextView collegeName;
        ImageView bcImage;
        LinearLayout clickcourse;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View itemtView, ViewGroup parent) {
        Course course;

        if(itemtView==null){
            course=new Course();
            //获得组件，实例化组件
            itemtView=layoutInflater.inflate(R.layout.item_courses,null);
            course.bcImage = itemtView.findViewById(R.id.bcImage);
            course.courseName = itemtView.findViewById(R.id.courseName);
            course.collegeName = itemtView.findViewById(R.id.collegeName);
            course.courseId = itemtView.findViewById(R.id.courseId);
            course.clickcourse = itemtView.findViewById(R.id.item_courses_id);
            //缓存
            itemtView.setTag(course);

        }else{
            course=(Course) itemtView.getTag();
        }
        //绑定数据
//        zujian.image.setBackgroundResource((Integer)data.get(position).get("image"));
//        zujian.title.setText((String)data.get(position).get("title"));
//        zujian.info.setText((String)data.get(position).get("info"));
        course.courseName.setText((String)data.get(position).get("courseName"));
        course.collegeName.setText((String)data.get(position).get("collegeName"));
        course.courseId.setText((String)data.get(position).get("courseId"));
        //todo 加载图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    URL url = new URL((String) data.get(position).get("coursePhoto"));
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                    Bitmap finalBitmap = bitmap;
                    course.bcImage.post(new Runnable() {
                        @Override
                        public void run() {
                            course.bcImage.setImageBitmap(finalBitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //单个课程被点击
        course.clickcourse.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        System.out.println("项目被点击，position为"+position);
//                        System.out.println("课号为："+data.get(position).get("courseId"));
                        int courseId = Integer.parseInt(data.get(position).get("courseId").toString());
                        BottomBarActivity.setCourseId(courseId);
                        int userId = Integer.parseInt(BottomBarActivity.getUser().getId());
                        //获取接口单个课程数据
                            String param = "/sign/course/detail?courseId="+courseId+"&userId="+userId;
                            Request request = MyUtil.get(param);
                            System.out.println(request);
                            Call call = MyUtil.getCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    Log.e(TAG,"onFailure"+e.toString());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(view.getContext(), "网络问题，课程数据加载!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    Log.e(TAG,"onResponse " + response.code());
                                    String responseJsonString = response.body().string();
                                    Log.e(TAG,"onResponse " + responseJsonString);
                                    if(MyResponse.getCode(responseJsonString).equals("200")){
                                        DetailCourse detailCourse1 = MyUtil.getJsonObject(responseJsonString,DetailCourse.class);
                                        Log.e(TAG,"detailCourse1.string " + detailCourse1.toString());
                                        //拿到课程数据后传入DetailCourse
//                                        setCourse(detailCourse1);
                                        DetailCourseActivity.course = detailCourse1;
                                        //添加成功后页面跳转
                                        Intent intent = new Intent(context, DetailCourseActivity.class);
                                        context.startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(view.getContext(), responseJsonString, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                    }
                }
        );
        return itemtView;
    }

}
