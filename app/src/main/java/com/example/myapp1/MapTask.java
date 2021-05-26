package com.example.myapp1;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapTask implements Runnable {

    private static final String TAG = "MyTask";
    Handler handler;

    public void setHandler(Handler h) {
        this.handler = h;
    }

    @Override
    public void run() {
        List<HashMap<String, String>> ret = new ArrayList<>();
        try {
            Thread.sleep(3000);
            Document doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run:" + doc.title());
            Element tables = doc.getElementsByTag("table").first();

            //按行取
            Elements trs = tables.getElementsByTag("tr");
            for(Element tr: trs){
                Elements tds = tr.getElementsByTag("td");
                if(tds.size() >=5) {
                    String str = tds.get(0).text();    //币种
                    String val = tds.get(5).text();  //汇率
                    float v = 100f / Float.parseFloat(val);
                    Log.i(TAG, "run:" + str + "->" + val);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("ItemTitle", str);  //标题文字
                    map.put("ItemDetail", String.format("%.2f",v));  //详情描述
                    ret.add(map);
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        // 将消息返回给主线程
        Message msg = handler.obtainMessage(9,ret);
        handler.sendMessage(msg);
    }
}
