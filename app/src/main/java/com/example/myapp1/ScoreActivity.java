package com.example.myapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    int score1=0;
    int score2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }
    private void show(){
        TextView out = findViewById(R.id.score1);
        out.setText(String.valueOf(score1));
        ((TextView)findViewById(R.id.score2)).setText(String.valueOf(score2));
    }

    public void btn3(View v){
        score1 += 3;
        show();
    }

    public void btn2(View v){
        score1 += 2;
        show();
    }

    public void btn1(View v){
        score1 += 1;
        show();
    }

    public void reset(View v){
        score1 = 0;
        score2 = 0;
        show();
    }


    public void btnb(View btn){
        if(btn.getId()==R.id.btnb3){
            score2 += 3;
            show();
        }
        else if(btn.getId()==R.id.btnb2){
            score2 += 2;
            show();
        }
        else {
            score2++;
            show();
        }
    }
}