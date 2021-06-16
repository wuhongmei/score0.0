package com.example.myapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList3Activity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "MyList3Activity";
    Handler handler;
    ListView listView3;
//    MyAdapter adapter;
    RateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list3);
        listView3 = findViewById(R.id.mylist3);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        listView3.setOnItemClickListener(this); //点击事件监听
        listView3.setOnItemLongClickListener(this); //长按事件监听

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // 收到消息后的处理
                Log.i(TAG, "handleMessage: 收到消息" + msg.what);
                if (msg.what == 11) {  // 确认对象
//                    SimpleAdapter listItemAdapter = new SimpleAdapter(MyList3Activity.this,
//                            listItems, //数据源
//                            R.layout.list_item, // 布局实现
//                            new String[] {"ItemTitle", "ItemDetail"},
//                            new int[] {R.id.itemTitle, R.id.itemDetail}
//                    );

                    //自定义适配器、自定义布局
//                    ArrayList<HashMap<String, String>> listItems = (ArrayList<HashMap<String, String>>)msg.obj;
//                    adapter = new MyAdapter(MyList3Activity.this, R.layout.list_item, listItems);
                    ArrayList<Rate> retlist = (ArrayList<Rate>)msg.obj;
                    adapter = new RateAdapter(MyList3Activity.this, R.layout.list_item, retlist);
                    listView3.setAdapter(adapter);
//                    listView3.setAdapter(listItemAdapter);
                    Toast.makeText(MyList3Activity.this, "Update Over", Toast.LENGTH_SHORT).show();
                    //处理显示与隐藏
                    progressBar.setVisibility(View.GONE);
                    listView3.setVisibility(View.VISIBLE);
                }
                super.handleMessage(msg);
            }
        };

//        MapTask task = new MapTask();
//        task.setHandler(handler);

        RateTask task = new RateTask();
        task.setHandler(handler);

        // 创建并开启子线程
        Thread t = new Thread(task);
//        Thread t = new Thread(this);
        t.start(); //task.run()


//        //构造数据
//        ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
//        for(int i = 0; i<10; i++){
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("ItemTitle", "Rate: " + i);  //标题文字
//            map.put("ItemDetail", "detail: " + i);  //详情描述
//            listItems.add(map);
//        }

//        SimpleAdapter listItemAdapter = new SimpleAdapter(MyList3Activity.this,
//                listItems, // listItems 数据源
//                R.layout.list_item, // list_item 的XML 布局实现
//                new String[] {"ItemTitle", "ItemDetail"},
//                new int[] {R.id.itemTitle, R.id.itemDetail}
//                );
//
//        listView3.setAdapter(listItemAdapter);

//        Handler handler = new Handler(Looper.myLooper()){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//            }
//        };
//
//        MyTask task = new Mytask();
//        task.setHandler(handler);
//
//        Thread t = new Thread(task);
//        t.start(); //调用task.run()
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: position=" + position);
        Object itemAtPosition = listView3.getItemAtPosition(position);

//        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
//        String titleStr = map.get("ItemTitle");
//        String detailStr = map.get("ItemDetail");
        Rate rate = (Rate)itemAtPosition;
        String titleStr = rate.getCname();
        String detailStr = rate.getCval();
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
//        Object itemAtPosition = listView3.getItemAtPosition(position);
        Rate rate = (Rate)listView3.getItemAtPosition(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", (dialog, which) -> {
                    Log.i(TAG, "onClick：对话框事件处理");
                    //删除数据项
//                    adapter.remove(itemAtPosition);
                    adapter.remove(rate);
                }).setNegativeButton("否", null);
        builder.create().show();
        return true; //不触发点击操作
    }

//    @SuppressLint("DefaultLocale")
//    @Override
//    public void run() {
//        // 改写run方法，实现子线程操作
////        ArrayList<HashMap<String, String>> ret = new ArrayList<>();
//        List<Rate> ret = new ArrayList<Rate>();
//        try {
//            Thread.sleep(3000);
//            Document doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
//            Log.i(TAG, "run:" + doc.title());
//            Element tables = doc.getElementsByTag("table").first(); // 找第一个表（汇率表）
//
//            //按行取
//            Elements trs = tables.getElementsByTag("tr");
//            for(Element tr: trs){
//                Elements tds = tr.getElementsByTag("td");
//                if(tds.size() >=5) {
//                    String str = tds.get(0).text();    //币种
//                    String val = tds.get(5).text();  //汇率
//                    float v = 100f / Float.parseFloat(val);
//                    Log.i(TAG, "run:" + str + "->" + val);
////                    HashMap<String, String> map = new HashMap<>();
////                    map.put("ItemTitle", str);  //标题文字
////                    map.put("ItemDetail", String.format("%.2f",v));  //详情描述
////                    ret.add(map);
//                    ret.add(new Rate(str, val));
//                }
//            }
//        } catch (InterruptedException | IOException e) {
//            e.printStackTrace();
//        }
//
//        // 将消息返回给主线程
//        Message msg = handler.obtainMessage(9,ret);
////        msg.what = 9;
////        msg.obj = ret;
//        handler.sendMessage(msg);
//    }
}