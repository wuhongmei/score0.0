package com.example.myapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ExchangeRateActivity extends AppCompatActivity {

//    private static final String TAG = "ExchangeRateActivity";
    EditText rmbEditor;
    Intent intent;
    String title, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);
        //获取汇率和币种数据
        intent = getIntent();
        title = intent.getStringExtra("title_key");
        detail = intent.getStringExtra("detail_key");

        //获取控件
        TextView result = findViewById(R.id.result);
        result.setText(title);
    }

    @SuppressLint("SetTextI18n")
    public void exchange(View view) {
        //获取控件
        rmbEditor = findViewById(R.id.input_tip);
        float rmb = Float.parseFloat(rmbEditor.getText().toString());

        //计算
        float v = Float.parseFloat(detail) * rmb;

        //将数据放入控件
        TextView result = findViewById(R.id.result);
        result.setText(v + title);
    }
}