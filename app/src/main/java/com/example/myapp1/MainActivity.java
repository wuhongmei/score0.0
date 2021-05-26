package com.example.myapp1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final EditText input;

    public MainActivity(EditText input) {
        this.input = input;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        show = findViewById(R.id.input_tip);
//        show.setText("请输入摄氏度：");
//        Button btn = findViewById(R.id.btn);
//        btn.setOnClickListener((View.OnClickListener) this);
    }


    @SuppressLint("SetTextI18n")
    public void exchange(View btn){
        //Log.i(TAG, "onClick:111");
        // 获取用户输入
        String inpStr = input.getText().toString();
        // 显示信息
        if(inpStr.length() > 0) {
            float t = ((Float.parseFloat(inpStr) * 9) / 5) + 32;
            TextView show = findViewById(R.id.calc_input);
            show.setText(t + "F");
        }
    }
}