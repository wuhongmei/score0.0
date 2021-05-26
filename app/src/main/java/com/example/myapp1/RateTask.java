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
import java.util.List;

public class RateTask implements Runnable {

    private static final String TAG = "RateTask";
    Handler handler;

    public void setHandler(Handler h) {
        this.handler = h;
    }

    @Override
    public void run() {
        List<Rate> ret = new ArrayList<>();
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
                    String str = tds.get(0).text();    //币种+
                    String val = tds.get(5).text();  //汇率
                    float v = 100f / Float.parseFloat(val);
                    Log.i(TAG, "run:" + str + "->" + val);
                    ret.add(new Rate(str, String.format("%.2f",v)));
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        // 将消息返回给主线程
        Message msg = handler.obtainMessage(11,ret);
        handler.sendMessage(msg);
    }
}
