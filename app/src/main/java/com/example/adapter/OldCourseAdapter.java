package com.example.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.picturesharing.R;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class OldCourseAdapter extends BaseAdapter {

    public List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public OldCourseAdapter(Context context,List<Map<String,Object>> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public final class OldCourse{
        TextView courseName;
        TextView courseId;
        TextView collegeName;
        ImageView bcImage;
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
        OldCourse oldCourse;
        if(itemtView==null){
            oldCourse=new OldCourse();
            //获得组件，实例化组件
            itemtView=layoutInflater.inflate(R.layout.item_courses,null);
            oldCourse.bcImage = itemtView.findViewById(R.id.bcImage);
            oldCourse.courseName = itemtView.findViewById(R.id.courseName);
            oldCourse.collegeName = itemtView.findViewById(R.id.collegeName);
            oldCourse.courseId = itemtView.findViewById(R.id.courseId);
            //缓存
            itemtView.setTag(oldCourse);
        }else{
            oldCourse=(OldCourse) itemtView.getTag();
        }
        oldCourse.courseName.setText((String)data.get(position).get("courseName"));
        oldCourse.collegeName.setText((String)data.get(position).get("collegeName"));
        oldCourse.courseId.setText((String)data.get(position).get("courseId"));
        //todo 加载图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    URL url = new URL((String) data.get(position).get("coursePhoto"));
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                    Bitmap finalBitmap = bitmap;
                    oldCourse.bcImage.post(new Runnable() {
                        @Override
                        public void run() {
                            oldCourse.bcImage.setImageBitmap(finalBitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return itemtView;
    }
}
