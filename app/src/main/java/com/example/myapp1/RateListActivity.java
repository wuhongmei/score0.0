package com.example.myapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateListActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "RateListActivity";
    Handler handler;
    ListView listView4;
    RateItemAdapter adapter;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list_);

        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i(TAG, "onCreate: lastRateDateStr=" + logDate);

        listView4 = findViewById(R.id.myList4);
        ProgressBar progressBar = findViewById(R.id.progressBar2);
        listView4.setOnItemClickListener(this); //点击事件监听
        listView4.setOnItemLongClickListener(this); //长按事件监听

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // 收到消息后的处理
                Log.i(TAG, "handleMessage: 收到消息" + msg.what);
                if (msg.what == 10) {  // 确认对象
                    //自定义适配器、自定义布局
                    ArrayList<RateItem> retList = (ArrayList<RateItem>)msg.obj;
                    adapter = new RateItemAdapter(RateListActivity.this, R.layout.list_item, retList);
                    listView4.setAdapter(adapter);
                    Toast.makeText(RateListActivity.this, "Update Over", Toast.LENGTH_SHORT).show();
                    //处理显示与隐藏
                    progressBar.setVisibility(View.GONE);
                    listView4.setVisibility(View.VISIBLE);
                }
                super.handleMessage(msg);
            }
        };

        // 创建并开启子线程
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        Log.i(TAG, "run: ...");
        List<RateItem> ret = new ArrayList<>();
        @SuppressLint("SimpleDateFormat") String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i(TAG, "run: curDateStr:" + curDateStr + "logDate" + logDate);
        if (curDateStr.equals(logDate)){
            //如果相等，则不从网络中获取数据
            Log.i(TAG, "run: 日期相等，从数据库中获取数据");
            DBManager dbManager = new DBManager(RateListActivity.this);
            ret.addAll(dbManager.listAll());
        }
        else{
            Log.i(TAG, "run: 日期不等，从网络中获取在线数据");
            try {
                List<RateItem> retList = new ArrayList<>(); //保存到数据库的数据
                Document doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
                Log.i(TAG, "run:" + doc.title());
                Element tables = doc.getElementsByTag("table").first();

                //按行取
                Elements trs = tables.getElementsByTag("tr");
                RateItem rateItem;
                for(Element tr: trs){
                    Elements tds = tr.getElementsByTag("td");
                    if(tds.size() >=5) {
                        String str = tds.get(0).text();    //币种+
                        String val = tds.get(5).text();  //汇率
                        float v = 100f / Float.parseFloat(val);
                        Log.i(TAG, "run:" + str + "->" + val);
                        rateItem = new RateItem(str, String.valueOf(v));
                        ret.add(rateItem);
                        retList.add(rateItem);
                    }
                }
                DBManager dbManager = new DBManager(RateListActivity.this);
                dbManager.deleteAll();
                Log.i(TAG, "run: db:删除所有记录");
                dbManager.addAll(retList);
                Log.i(TAG, "run: db:添加新记录集");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //更新记录日期
            SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(DATE_SP_KEY, curDateStr);
            edit.apply();
            Log.i(TAG, "run: 更新日期结束：" + curDateStr);
        }

        // 将消息返回给主线程
        Message msg = handler.obtainMessage(10,ret);
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: position=" + position);
        Object itemAtPosition = listView4.getItemAtPosition(position);

        RateItem rateItem = (RateItem)itemAtPosition;
        String titleStr = rateItem.getCurName();
        String detailStr = rateItem.getCurRate();
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);

        //打开窗口，进行参数传递
        Intent exchange = new Intent(this, ExchangeRateActivity.class);
        //参数传递
        exchange.putExtra("title_key", titleStr);
        exchange.putExtra("detail_key", detailStr);
        startActivity(exchange);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //长按操作
        Log.i(TAG, "onItemLongClick：长按操作");
        RateItem rateItem = (RateItem)listView4.getItemAtPosition(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", (dialog, which) -> {
                    Log.i(TAG, "onClick：对话框事件处理");

                    adapter.remove(rateItem);
                }).setNegativeButton("否", null);
        builder.create().show();
        return true; //不触发点击操作
    }
}