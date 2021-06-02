package com.example.myapp1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "myrate.db";
    public static final String TB_NAME = "tb_rates";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //对数据库的初始化操作
        String sql = "CREATE TABLE " + TB_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, CURNAME TEXT, CURRATE TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion==1 && newVersion==2){
            db.execSQL("");
        }
        else if (oldVersion==3 && newVersion==5){
            db.execSQL("");
        }
    }
}
