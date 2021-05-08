package com.example.myapp1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;


public class RateActivity extends AppCompatActivity implements Runnable {

    private static final String TAG = "RateActivity";
    float dollarRate = 0.0f;
    float euroRate = 0.0f;
    float wonRate = 0.0f;
    Handler handler; //线程间消息同步

    @Override
    protected void onCreate(Bundle savedInstanceState) {  //创建时的操作
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        //读取保存在文件中的数据（使用SharedPreferences对象），保存到myrate文件
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE); //保存数据的文件
        dollarRate = sharedPreferences.getFloat("dollar_rate", 0.15f);
        euroRate = sharedPreferences.getFloat("euro_rate", 0.12f);
        wonRate = sharedPreferences.getFloat("won_rate", 170.21f);


        Log.i(TAG, "onCreate: dollarRate=" + dollarRate);
        Log.i(TAG, "onCreate: euroRate=" + euroRate);
        Log.i(TAG, "onCreate: wonRate=" + wonRate);

        Date d2 = new Date(); //当前时间
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String d2_str = sdf.format(d2);
        Calendar c = Calendar.getInstance();
        c.setTime(d2);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date d = c.getTime(); //前一天
        String d_str = sdf.format(d);
        String D1 = sharedPreferences.getString("update_date", d_str); //获取上次更新日期，否则返回前一天

        if(!D1.equals(d2_str)) { //实现每天更新一次
            // 创建并开启子线程
            Thread t = new Thread(this);
            t.start();

            handler = new Handler(Looper.myLooper()) {
                // 设置当前管理循环器，向主线程传递消息
                @Override
                public void handleMessage(@NonNull Message msg) {
                    // 收到消息后的处理
                    Log.i(TAG, "handleMessage: 收到消息" + msg.what);
                    if (msg.what == 7) {  // 确认对象
                        //                    String str = (String)msg.obj;
                        //                    Log.i(TAG, "handleMessage: get str=" + str);
                        //                    TextView result = findViewById(R.id.result);
                        //                    result.setText(str);
                        Bundle bdl = (Bundle) msg.obj;
                        dollarRate = bdl.getFloat("dollar-rate");
                        euroRate = bdl.getFloat("euro-rate");
                        wonRate = bdl.getFloat("won-rate");
                        Log.i(TAG, "handleMessage: dollar=" + dollarRate);
                        Log.i(TAG, "handleMessage: euro=" + euroRate);
                        Log.i(TAG, "handleMessage: won=" + wonRate);
                        Toast.makeText(RateActivity.this, "汇率已更新", Toast.LENGTH_SHORT).show();
                    }
                    super.handleMessage(msg);
                }
            };

            //保存更新的日期和汇率
            Date d1 = new Date(); //当前时间
            String d1_str = sdf.format(d1);
            SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("dollar_rate", dollarRate);
            editor.putFloat("euro_rate", euroRate);
            editor.putFloat("won_rate", wonRate);
            editor.putString("update_date", d1_str); //保存更新的时间
            editor.apply();
        }
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

    public void openConfig(View btn) {
        //作为事件处理的方法，必须是public
        //打开另一个Activity页面
        Log.i(TAG, "openConfig: ");  //调试时用
        open();
    }

    private void open() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //启用菜单项
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.menu_set){
            //事件处理代码
            open();
        }
        return super.onOptionsItemSelected(item);
    }

    private String inputStream2String(InputStream inputStream) throws IOException{
        // 将网页文本数据转换成字符串类型
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312"); //根据网站变化
        while(true){
            int rsz = in.read(buffer, 0, buffer.length);
            if(rsz < 0) break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public void run(){
        // 改写run方法，实现子线程操作
//        Log.i(TAG, "run: ");
//        // 耗时3秒的工作
//        try {
//            Thread.sleep(3000);
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 获取网络中的数据
//        URL url;
//        try{
//            url = new URL("http://www.usd-cny.com/bankofchina.htm");
//            HttpURLConnection http = (HttpURLConnection)url.openConnection();
//            InputStream in = http.getInputStream();
//
//            String html = inputStream2String(in);
//            Log.i(TAG, "run: html=" + html);
//        } catch(MalformedURLException e){
//            e.printStackTrace();
//        } catch(IOException e){
//            e.printStackTrace();
//        }

        //用于保存获取的汇率
        Bundle bundle = new Bundle();
        try{
            Document doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run:" + doc.title());
            Element tables = doc.getElementsByTag("table").first(); // 找第一个表（汇率表）

            //按表格取
//            Elements tds = tables.getElementsByTag("td");
//            for(int i=0;i<tds.size();i+=6) {  // 第一列是币种，第六列是汇率，共六列
//                Element td1 = tds.get(i);
//                Element td2 = tds.get(i+5);
//                Log.i(TAG, "run: " + td1.text() + "=>" + td2.text());
//            }

//            for(Element td : tds){
//                Log.i(TAG, "run: td=" + td);
//            }
//            Elements cls = doc.getElementsByClass("bz"); //该网页每一行第一列的class为bz
//            for(Element c : cls){
//                Log.i(TAG, "run: c=" + c);
//                Log.i(TAG, "run: c.html=" + c.html());
//                Log.i(TAG, "run: c.text=" + c.text());
//            }

            //按行取
            Elements trs = tables.getElementsByTag("tr");
            for(Element tr: trs){
                Elements rtds = tr.getElementsByTag("td");
                if(rtds.size() >=5) { //第一行为表头
                    String str = rtds.get(0).text();    //币种
                    String val = rtds.get(5).text();  //汇率
                    float v = 100f / Float.parseFloat(val);
                    Log.i(TAG, "run:" + str + "->" + val);
                    if("美元".equals(str)){
                        bundle.putFloat("dollar-rate", v);
                        Log.i(TAG, "run:美元->" + val);
                    }
                    else if("欧元".equals(str)){
                        bundle.putFloat("euro-rate", v);
                        Log.i(TAG, "run:欧元->" + val);
                    }
                    else if("韩元".equals(str)){
                        bundle.putFloat("won-rate", v);
                        Log.i(TAG, "run:韩元->" + val);
                    }
                }
            }

            //通过select选取（输出集合）
//            Element ele = doc.select("body > section > div > div > article > table > tbody > tr:nth-child(8) > td:nth-child(6)").first();
//            Log.i(TAG, "run: 欧元=" + ele.text());
//            Element ele2 = doc.select("table > tbody > tr:nth-child(14) > td:nth-child(6)").first();
//            Log.i(TAG, "run: 韩元=" + ele2.text());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //bundle中保存获取的汇率

        // 将消息返回给主线程
        Message msg = handler.obtainMessage();
        msg.what = 7;
        msg.obj = bundle;
        //msg.obj = "Hello from handler";
        handler.sendMessage(msg);
    }
}