package com.example.myapp1;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FirstListActivity extends ListActivity implements Runnable {
    private static final String TAG = "FirstListActivity";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //不能用setContentView(R.layout.activity_rate_list);
        //数据项
//        List<String> list1 = new ArrayList<String>();
//        for(int i = 1; i<100; i++){
//            list1.add("item" + i);
//        }

//        ListAdapter adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1,list1); //行布局
//        setListAdapter(adapter);

        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==5) {
                    List<String> retList = (ArrayList<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(
                            FirstListActivity.this,
                            android.R.layout.simple_list_item_1, retList);
                    setListAdapter(adapter);
                    Toast.makeText(FirstListActivity.this, "Update Over", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

//        MyTask task = new MyTask();
//        task.setHandler(handler);

        RateActivity r = new RateActivity();
        r.setHandler(handler);

        Thread t = new Thread(this); // = new Thread(r)  = new Thread(task)
        t.start(); //调用RateActivity中的run方法r.run()
    }

    public void run(){
        // 改写run方法，实现子线程操作
        //用于保存获取的汇率
        Bundle bundle = new Bundle();
        List<String> retList = new ArrayList<String>();
        try{
            Document doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run:" + doc.title());
            Element tables = doc.getElementsByTag("table").first(); // 找第一个表（汇率表）

            //按行取
            Elements trs = tables.getElementsByTag("tr");
            for(Element tr: trs){
                Elements tds = tr.getElementsByTag("td");
                if(tds.size() >=5) {
                    String str = tds.get(0).text();    //币种
                    String val = tds.get(5).text();  //汇率
                    float v = 100f / Float.parseFloat(val);
                    Log.i(TAG, "run:" + str + "->" + val);
                    retList.add(str + "->" + val);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //bundle中保存获取的汇率
        // 将消息返回给主线程
        Message msg = handler.obtainMessage();
        msg.what = 5;
        msg.obj = retList;
        handler.sendMessage(msg);
    }
}