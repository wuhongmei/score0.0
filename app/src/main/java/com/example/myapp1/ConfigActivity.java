package com.example.myapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {

    private static final String TAG = "ConfigActivity";
    EditText dollarEditor, euroEditor, wonEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //get data
        Intent intent = getIntent();
        float dollar2 = intent.getFloatExtra("dollar_key", 0.0f);
        float euro2 = intent.getFloatExtra("euro_key", 0.0f);
        float won2 = intent.getFloatExtra("won_key", 0.0f);

        Log.i(TAG, "onCreate: ConfigActivity dollar2=" + dollar2);
        Log.i(TAG, "onCreate: ConfigActivity euro2=" + euro2);
        Log.i(TAG, "onCreate: ConfigActivity won2=" + won2);

        //获取控件
        dollarEditor = findViewById(R.id.edit_dollar);
        euroEditor = findViewById(R.id.edit_euro);
        wonEditor = findViewById(R.id.edit_won);

        //将数据放入控件
        dollarEditor.setText(String.valueOf(dollar2));
        euroEditor.setText(String.valueOf(euro2));
        wonEditor.setText(String.valueOf(won2));
    }

    public void save(View btn){
        Log.i(TAG, "save: ");
        //获取用户新输入的数据项，保存在变量中
        float newdollar = Float.parseFloat(dollarEditor.getText().toString());
        float neweuro = Float.parseFloat(euroEditor.getText().toString());
        float newwon = Float.parseFloat(wonEditor.getText().toString());

        Log.i(TAG, "save: newdollar=" + newdollar);
        Log.i(TAG, "save: neweuro=" + neweuro);
        Log.i(TAG, "save: newwon=" + newwon);

        //保存到文件：data文件下data文件下（AndroidManifest.xml中包名）com.example.myapp1下shared_prefs下的myrate.xml文件中
        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("dollar_rate", newdollar);
        editor.putFloat("euro_rate", neweuro);
        editor.putFloat("won_rate", newwon);
        editor.apply();

        //返回数据到调用页面
        Intent ret = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_key", newdollar);
        bdl.putFloat("euro_key", neweuro);
        bdl.putFloat("won_key", newwon);
        ret.putExtras(bdl);
        setResult(1,ret);
        //关闭窗口
        finish();
    }
}