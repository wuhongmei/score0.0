package com.example.myapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
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

public class GridViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        GridView gridView = findViewById(R.id.my_grid_view1);

        List<String> list1 = new ArrayList<>();
        for(int i = 1; i<100; i++){
            list1.add("item" + i);
        }

        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list1);

        gridView.setAdapter(adapter);
        gridView.setEmptyView(findViewById(R.id.no_data_tip)); //没有数据则显示该控件
    }
}