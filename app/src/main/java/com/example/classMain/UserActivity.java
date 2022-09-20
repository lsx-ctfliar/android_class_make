package com.example.classMain;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.picturesharing.R;


//
public class UserActivity extends Fragment {

    public static final String TAG = "UserActivity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        BottomBarActivity bottomBarActivity = (BottomBarActivity)getActivity(); //获取主活动中的对象

        //该View表示该碎片的主界面,最后要返回该view
        View view=inflater.inflate(R.layout.activity_user,container,false);
        //找到主界面view后，就可以进行UI的操作了。
        //注意：因为主界面现在是view，所以在找寻控件时要用view.findViewById
        TextView realName =(TextView) view.findViewById(R.id.user_realName);
        TextView userName =(TextView) view.findViewById(R.id.user_userName);
        ImageView imageView =(ImageView) view.findViewById(R.id.avatar);
        RelativeLayout updateInfo =(RelativeLayout) view.findViewById(R.id.update_info);
        Button logOff = (Button) view.findViewById(R.id.log_off);
        //设置User数据
        realName.setText(bottomBarActivity.getUser().getRealName());
        userName.setText(bottomBarActivity.getUser().getUserName());

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update = new Intent(getActivity(),UpdateUserActivity.class);
                update.putExtra("user",bottomBarActivity.getUserJsonString());
                startActivity(update);
            }
        });

        logOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }



}