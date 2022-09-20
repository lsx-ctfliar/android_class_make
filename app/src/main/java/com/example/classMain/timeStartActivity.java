package com.example.classMain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.picturesharing.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class timeStartActivity extends AppCompatActivity {
    String hours;
    String minutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_start);
        //时间选择器
        TimePicker timePicker=findViewById(R.id.Starttime_picker);
        timePicker.setIs24HourView(true);
        Button addTime = findViewById(R.id.addStartTime);
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd"); //设置时间格式
                formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区
                Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
                String createDate = formatter.format(curDate);   //格式转换

                if(hour<10)
                    hours = "0"+Integer.toString(hour) ;
                else
                    hours = Integer.toString(hour);
                if(minute<10)
                    minutes = '0'+Integer.toString(minute);
                else
                    minutes = Integer.toString(minute);

                TeacherCourseRegisterActivity.starTime =createDate+" "+hours+":"+minutes;
//                String time = "2022-10-16 08:27";
                Date date = null;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    date = simpleDateFormat.parse(createDate+" "+hour+":"+minute);
                    System.out.println("date"+date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("time"+date.getTime());
                TeacherCourseRegisterActivity.stTime = date.getTime();
//                Toast.makeText(getApplicationContext(), hour+"-"+minute, Toast.LENGTH_SHORT).show();

//                DateFormat.getTimeFormat(getBaseContext()).format(new Date(timePicker.getMinute()));
                finish();
            }
        });
    }
}
