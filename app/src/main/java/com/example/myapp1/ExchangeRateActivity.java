package com.example.myapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ExchangeRateActivity extends AppCompatActivity {

//    String TAG = "ExchangeRateActivity";
    float rate = 0f;
    EditText inp2;
    Intent intent;
    String title, detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);
        //获取汇率和币种数据
        intent = getIntent();
        title = intent.getStringExtra("title_key");
        rate = Float.parseFloat(intent.getStringExtra("detail_key"));

        //获取控件
        ((TextView)findViewById(R.id.result)).setText(title);
        inp2 = findViewById(R.id.calc_input);
        inp2.addTextChangedListener(new TextWatcher() {  //输入时响应
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                TextView show = ExchangeRateActivity.this.findViewById(R.id.show2);
                if(s.length()>0){
                    float val = Float.parseFloat(s.toString());
                    show.setText(val + "RMB-->" + (rate * val));
                }else{
                    show.setText("");
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void exchange(View view) {
        //获取控件
        inp2 = findViewById(R.id.calc_input);
        float rmb = Float.parseFloat(inp2.getText().toString());

        //计算
        float v = rate * rmb;

        //将数据放入控件
        TextView result = findViewById(R.id.result);
        result.setText(v + title);
    }
}