package com.example.helloweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    public static SQLiteDatabase database;
    //初始化数据库
    public static void initDB(Context context){
        DBHelper dbHelper = new DBHelper(context);
    }
}
