package com.example.myapp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RateAdapter extends ArrayAdapter {
    public RateAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Rate> list) {
        super(context, resource, list);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Rate rate = (Rate) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
        TextView detail = (TextView) itemView.findViewById(R.id.itemDetail);
        title.setText(rate.getCname());
        detail.setText(rate.getCval());
        return itemView;
    }
}
