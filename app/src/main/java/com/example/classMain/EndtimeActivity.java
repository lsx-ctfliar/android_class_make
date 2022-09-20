package com.example.classMain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.picturesharing.R;

public class EndtimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endtime);
        //时间选择器
        DatePicker datePicker=findViewById(R.id.datepickerEnd);
        Button addTime = findViewById(R.id.addEndTime);
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year=datePicker.getYear();
                int month=datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                AddCourseActivity.endTime = year+"年"+month+"月"+day+"日";
                Log.d("info", "onClick: -----------"+AddCourseActivity.endTime);
                finish();
            }
        });
    }
}