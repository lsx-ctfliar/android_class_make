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
import com.example.picturesharing.R;
import com.example.classMain.TeacherCourseDetailActivity;
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

public class RecentCourseAdapter extends BaseAdapter {

    private static final String TAG = "RecentCourseAdapter";
    public List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public RecentCourseAdapter(Context context,List<Map<String,Object>> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public final class RecentCourse{
        TextView courseName;
        TextView courseId;
        TextView collegeName;
        ImageView bcImage;
        LinearLayout recentlyCourses;
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
        RecentCourse recentCourse;
        if(itemtView==null){
            recentCourse=new RecentCourse();
            //??????????????????????????????
            itemtView=layoutInflater.inflate(R.layout.item_courses,null);
            recentCourse.bcImage = itemtView.findViewById(R.id.bcImage);
            recentCourse.courseName = itemtView.findViewById(R.id.courseName);
            recentCourse.collegeName = itemtView.findViewById(R.id.collegeName);
            recentCourse.courseId = itemtView.findViewById(R.id.courseId);
            recentCourse.recentlyCourses= itemtView.findViewById(R.id.item_courses_id);
            //??????
            itemtView.setTag(recentCourse);
        }else{
            recentCourse=(RecentCourse) itemtView.getTag();
        }
        recentCourse.courseName.setText((String)data.get(position).get("courseName"));
        recentCourse.collegeName.setText((String)data.get(position).get("collegeName"));
        recentCourse.courseId.setText((String)data.get(position).get("courseId"));
        //todo ????????????
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    URL url = new URL((String) data.get(position).get("coursePhoto"));
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                    Bitmap finalBitmap = bitmap;
                    recentCourse.bcImage.post(new Runnable() {
                        @Override
                        public void run() {
                            recentCourse.bcImage.setImageBitmap(finalBitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // ??????????????????
        recentCourse.recentlyCourses.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    //RecentCouseId??????????????????
                    public void onClick(View view) {
                        int courseId = Integer.parseInt(data.get(position).get("courseId").toString());
                        BottomBarActivity.setCourseId(courseId);
                        toCourse(courseId,Integer.parseInt(BottomBarActivity.getUser().getId()));
                    }
                }
        );
        return itemtView;
    }

    //?????????????????????????????????????????????
    public void toCourse(int get_courseId,int get_userId)
    {
        String param = "/sign/course/detail?courseId="+get_courseId+"&userId="+get_userId;
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
                        Toast.makeText(context.getApplicationContext(), "?????????????????????????????????!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e(TAG,"onResponse " + response.code());
                String responseJsonString = response.body().string();
                Log.e(TAG,"onResponse " + responseJsonString);
                if(MyResponse.getCode(responseJsonString).equals("200")){
                    TeacherCourseDetailActivity.detailCourse= MyUtil.getJsonObject(responseJsonString,DetailCourse.class);
                    Intent intent = new Intent(context, TeacherCourseDetailActivity.class);
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context.getApplicationContext(), responseJsonString, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
