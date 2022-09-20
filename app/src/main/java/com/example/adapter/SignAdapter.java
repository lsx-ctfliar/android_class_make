package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.classMain.BottomBarActivity;
import com.example.classMain.QiandaoActivity;
import com.example.picturesharing.R;

import java.util.List;
import java.util.Map;

public class SignAdapter extends BaseAdapter {
    public List<Map<String,Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public SignAdapter(Context context,List<Map<String,Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public final class Sign {
        TextView userSignId;
        TextView courseName;
        TextView courseAddr;
        TextView createTime;
        Button go_qiandao;
        RelativeLayout item_sign_id;
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
    public View getView(int position, View itemView, ViewGroup viewGroup) {
        Sign sign;
        if(itemView==null){
            sign = new Sign();
            //获得组件，实例化组件
            itemView = layoutInflater.inflate(R.layout.item_sign,null);
            sign.userSignId = itemView.findViewById(R.id.userSign_id);
            sign.courseName = itemView.findViewById(R.id.course_name);
            sign.courseAddr = itemView.findViewById(R.id.course_addr);
            sign.createTime = itemView.findViewById(R.id.create_time);
            sign.go_qiandao = itemView.findViewById(R.id.go_qiandao);
            sign.item_sign_id = itemView.findViewById(R.id.item_sign_id);
            itemView.setTag(sign);
        }else{
            sign = (Sign) itemView.getTag();
        }


        sign.go_qiandao.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(data);
                int userSignId = Integer.parseInt(data.get(position).get("userSignId").toString());
                String courseName = data.get(position).get("courseName").toString();
                String courseAddr = data.get(position).get("courseAddr").toString();
                String createTime = data.get(position).get("SignTime").toString();

                System.out.println("已获取数据-----------");
                BottomBarActivity.setUserSignId(userSignId);
                BottomBarActivity.setCourseName(courseName);
                BottomBarActivity.setCourseAddr(courseAddr);
                BottomBarActivity.setCreateTime(createTime);

                System.out.println("界面已经跳转！------------");
                Intent intent = new Intent();
                intent.setClass(context, QiandaoActivity.class);
                context.startActivity(intent);

            }
        });

        sign.userSignId.setText((String)data.get(position).get("userSignId"));
        sign.courseName.setText((String)data.get(position).get("courseName"));
        sign.courseAddr.setText((String)data.get(position).get("courseAddr"));
        sign.createTime.setText((String)data.get(position).get("createTime"));
        return itemView;
    }
}
