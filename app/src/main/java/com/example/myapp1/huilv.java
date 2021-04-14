package com.example.myapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class huilv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huilv);
    }

    public void exchange(View btn){
        //获取输入内容
        EditText rmb = findViewById(R.id.input_tip);
        String str = rmb.getText().toString();
        float m = Float.parseFloat(str);
        float r;
        TextView result = findViewById(R.id.result);
        //判断用户输入是否为空
        if(str.length() > 0){
            if(btn.getId()==R.id.btn_dollar){
                r = 0.15f;
            }
            else if(btn.getId()==R.id.btn_euro){
                r = 0.12f;
            }
            else r = 170.25f;
            @SuppressLint("DefaultLocale") String rm = String.format("%.2f", m * r);
            result.setText(rm);
        }
    }
}