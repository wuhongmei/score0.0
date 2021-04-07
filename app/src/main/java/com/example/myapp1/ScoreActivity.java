package com.example.myapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ScoreActivity extends AppCompatActivity {
    int score=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }

}