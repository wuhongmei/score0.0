package com.example.myapp1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RateActivity extends AppCompatActivity {

    private static final String TAG = "RateActivity";
    float dollarRate = 0.15f;
    float euroRate = 0.12f;
    float wonRate = 170.21f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
    }

    public void exchange(View btn){
        //获取输入内容
        EditText rmb = findViewById(R.id.input_tip);
        String str = rmb.getText().toString();
        TextView result = findViewById(R.id.result);
        //判断用户输入是否为空
        if(str.length() > 0){
            //判断是哪个按钮，设置相应汇率
            float r;
            if(btn.getId()==R.id.btn_dollar){
                r = dollarRate;
            }
            else if(btn.getId()== R.id.btn_euro){
                r = euroRate;
            }
            else r = wonRate;
            @SuppressLint("DefaultLocale") String rm = String.format("%.2f", Float.parseFloat(str) * r);
            result.setText(rm);
        }
        else{
            //提示需要输入内容
            Toast.makeText(this, "请输入相应金额", Toast.LENGTH_SHORT).show();
        }
    }

    public void openConfig(View btn) { //作为事件处理的方法，必须是public
        //打开另一个Activity页面
        Log.i(TAG, "openConfig: ");  //调试时用
        Intent config = new Intent(this, ConfigActivity.class);
        //参数传递
        config.putExtra("dollar_key", dollarRate);
        config.putExtra("euro_key", euroRate);
        config.putExtra("won_key", wonRate);
//        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));  //访问网页
//        Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:17712345678"));  //拨号
        Log.i(TAG, "openConfig: dollarRate" + dollarRate);
        Log.i(TAG, "openConfig: euroRate" + euroRate);
        Log.i(TAG, "openConfig: wonRate" + wonRate);
        //startActivity(config);
        startActivityForResult(config,3); //打开一个可以返回数据的窗口
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //获取返回的数据
        if(requestCode==3 && resultCode==1){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("dollar_key",0.0f);
            euroRate = bundle.getFloat("euro_key",0.0f);
            wonRate = bundle.getFloat("won_key",0.0f);

            Log.i(TAG, "onActivityResult: dollarRate" + dollarRate);
            Log.i(TAG, "onActivityResult: euroRate" + euroRate);
            Log.i(TAG, "onActivityResult: wonRate" + wonRate);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}