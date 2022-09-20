package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.classMain.BottomBarActivity;
import com.example.picturesharing.R;
import com.example.classMain.SignActivity;

import android.view.View.OnClickListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class StudentCoursesAdapter extends BaseAdapter implements OnClickListener{
    public List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private InnerItemOnclickListener mListener;

    public StudentCoursesAdapter(Context context,List<Map<String,Object>> data){
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public final class StudentCourse{
        TextView courseName;
        TextView courseId;
        TextView collegeName;
        ImageView bcImage;
        Button deleteClass;
        LinearLayout LL;
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

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }
    @Override
    public View getView(int position, View itemtView, ViewGroup parent) {
        StudentCourse studentCourse;
        if(itemtView==null){
            studentCourse=new StudentCourse();
            //获得组件，实例化组件
            itemtView=layoutInflater.inflate(R.layout.item_course_student,null);
            studentCourse.bcImage = itemtView.findViewById(R.id.bcImage_student);
            studentCourse.courseName = itemtView.findViewById(R.id.courseName_student);
            studentCourse.collegeName = itemtView.findViewById(R.id.collegeName_student);
            studentCourse.courseId = itemtView.findViewById(R.id.courseId_student);
            studentCourse.deleteClass = itemtView.findViewById(R.id.delete_class_student);
            studentCourse.LL = itemtView.findViewById(R.id.item_student_courses_id);
            //缓存
            itemtView.setTag(studentCourse);
        }else{
            studentCourse=(StudentCourse) itemtView.getTag();
        }
        studentCourse.courseName.setText((String)data.get(position).get("courseName"));
        studentCourse.collegeName.setText((String)data.get(position).get("collegeName"));
        studentCourse.courseId.setText((String)data.get(position).get("courseId"));
        studentCourse.deleteClass.setOnClickListener(this);
        studentCourse.deleteClass.setTag(position);
        //todo 加载图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    URL url = new URL((String) data.get(position).get("coursePhoto"));
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                    Bitmap finalBitmap = bitmap;
                    studentCourse.bcImage.post(new Runnable() {
                        @Override
                        public void run() {
                            studentCourse.bcImage.setImageBitmap(finalBitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        studentCourse.LL.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("项目被点击，position为"+position);
                System.out.println("课号为："+data.get(position).get("courseId"));
                int courseId = Integer.parseInt(data.get(position).get("courseId").toString());
                BottomBarActivity.setCourseId2(courseId);
                System.out.println("拿取到的courseId的数据为=>"+BottomBarActivity.getCourseId2());
                Log.e("courseId",Integer.toString(courseId));
                Intent intent = new Intent(context, SignActivity.class);
                context.startActivity(intent);
            }
        });
        return itemtView;
    }
    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    public void onClick(View v) {
        mListener.itemClick(v);
    }

}
