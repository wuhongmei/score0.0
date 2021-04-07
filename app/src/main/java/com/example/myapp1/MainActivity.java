package com.example.myapp1;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView show;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        show = findViewById(R.id.textView);
        show.setText("请输入摄氏度：");

        EditText input1 = findViewById(R.id.ipt);
        String inpStr = input.getText().toString();

//        Log.i("abc", "onCreate: log from activity");  //用于检查
//        Log.i(TAG, "onCreate: log from activity");
//        Log.d(TAG, "onCreate: asdf");

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener((View.OnClickListener) this);
    }

    public void onClick(View v){
        //Log.i(TAG, "onClick:111");
        // 获取用户输入
        String inpStr = input.getText().toString();
        // 显示信息
        if(inpStr != null && inpStr.length()>0) {
            float t = ((Float.parseFloat(inpStr) * 9) / 5) + 32;
            show.setText(String.valueOf(t) + "F");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //TextView show = findViewById(R.id.textView);
    //show.setText("设置的名称");
    //String inpStr = textView.getText().toString();

}