package com.example.helloweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context,"weather.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String sql = "create table info(id integer primary key autoincrement,cityName varchar(20), cityCode varchar(25), condition varchar(20), curTemp varchar(5), " +
                "dressIndex varchar(20), sunIndex varchar(20), coldIndex varchar(20), umbrellaIndex varchar(20), carIndex varchar(20), exerciseIndex varchar(20), " +
                "todayIcon varchar(4), todayMinTemp varchar(4), todayMaxTemp varchar(4), todayCondition varchar(20), " +
                "tomorrowIcon varchar(4), tomorrowMinTemp varchar(4), tomorrowMaxTemp varchar(4), tomorrowCondition varchar(20), " +
                "nextIcon varchar(4), nextMinTemp varchar(4), nextMaxTemp varchar(4), nextCondition varchar(20))";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
