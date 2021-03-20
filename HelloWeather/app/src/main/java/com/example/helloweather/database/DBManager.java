package com.example.helloweather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArraySet;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBManager {
    private static final String TAG = "DBManager";
    private static final String TABLE_NAME = "weather";

    public static SQLiteDatabase database;

    //初始化数据库
    public static void initDB(Context context){
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    //查找数据库当中所有城市名字
    public static List<String> queryAllCityName(){
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        List<String> cityList = new ArrayList<String>();
        while (cursor.moveToNext()){
            String cityName = cursor.getString(cursor.getColumnIndex("cityName"));
            cityList.add(cityName);
        }

        return cityList;
    }

    //查询数据库中的所有城市的所有信息
    public static List<DataBaseBean> queryAllInfo(){
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        List<DataBaseBean> cityList = new ArrayList<DataBaseBean>();
        while (cursor.moveToNext()){
            DataBaseBean bean = new DataBaseBean();
            bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
            bean.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
            bean.setCityCode(cursor.getString(cursor.getColumnIndex("cityCode")));
            bean.setCurTemp(cursor.getString(cursor.getColumnIndex("curTemp")));
            bean.setCondition(cursor.getString(cursor.getColumnIndex("condition")));
            bean.setDressIndex(cursor.getString(cursor.getColumnIndex("dressIndex")));
            bean.setColdIndex(cursor.getString(cursor.getColumnIndex("coldIndex")));
            bean.setSunIndex(cursor.getString(cursor.getColumnIndex("sunIndex")));
            bean.setCarIndex(cursor.getString(cursor.getColumnIndex("carIndex")));
            bean.setExerciseIndex(cursor.getString(cursor.getColumnIndex("exerciseIndex")));
            bean.setUmbrellaIndex(cursor.getString(cursor.getColumnIndex("umbrellaIndex")));
            bean.setTodayIcon(cursor.getString(cursor.getColumnIndex("todayIcon")));
            bean.setTodayCondition(cursor.getString(cursor.getColumnIndex("todayCondition")));
            bean.setTodayMinTemp(cursor.getString(cursor.getColumnIndex("todayMinTemp")));
            bean.setTodayMaxTemp(cursor.getString(cursor.getColumnIndex("todayMaxTemp")));
            bean.setTomorrowIcon(cursor.getString(cursor.getColumnIndex("tomorrowIcon")));
            bean.setTomorrowCondition(cursor.getString(cursor.getColumnIndex("tomorrowCondition")));
            bean.setTomorrowMinTemp(cursor.getString(cursor.getColumnIndex("tomorrowMinTemp")));
            bean.setTomorrowMaxTemp(cursor.getString(cursor.getColumnIndex("tomorrowMaxTemp")));
            bean.setNextIcon(cursor.getString(cursor.getColumnIndex("nextIcon")));
            bean.setNextCondition(cursor.getString(cursor.getColumnIndex("nextCondition")));
            bean.setNextMinTemp(cursor.getString(cursor.getColumnIndex("nextMinTemp")));
            bean.setNextMaxTemp(cursor.getString(cursor.getColumnIndex("nextMaxTemp")));
            cityList.add(bean);
        }
        return cityList;
    }

    /**
     * 判断是否重复添加
     * return 1为数据库中已存在
     */
    public static boolean isExist(String cityName){
        Cursor cursor = database.query(TABLE_NAME,null, "cityName=?", new String[]{cityName},null, null, null);
        if (cursor.getColumnCount() == 0){
            return false;
        }
        return true;
    }

    //新增城市信息
    public static long addCityComplete(DataBaseBean bean){
        ContentValues values = new ContentValues();
        values.put("cityName",bean.getCityName());
        values.put("cityCode",bean.getCityCode());
        values.put("condition",bean.getCondition());
        values.put("curTemp",bean.getCurTemp());
        values.put("dressIndex",bean.getDressIndex());
        values.put("sunIndex",bean.getSunIndex());
        values.put("coldIndex",bean.getColdIndex());
        values.put("umbrellaIndex",bean.getUmbrellaIndex());
        values.put("carIndex",bean.getCarIndex());
        values.put("exerciseIndex",bean.getExerciseIndex());
        values.put("todayIcon",bean.getTodayIcon());
        values.put("todayMinTemp",bean.getTodayMinTemp());
        values.put("todayMaxTemp",bean.getTodayMaxTemp());
        values.put("todayCondition",bean.getTodayCondition());
        values.put("tomorrowIcon",bean.getTomorrowIcon());
        values.put("tomorrowMinTemp",bean.getTomorrowMinTemp());
        values.put("tomorrowMaxTemp",bean.getTomorrowMaxTemp());
        values.put("tomorrowCondition",bean.getTomorrowCondition());
        values.put("nextIcon",bean.getNextIcon());
        values.put("nextMinTemp",bean.getNextMinTemp());
        values.put("nextMaxTemp",bean.getNextMaxTemp());
        values.put("nextCondition",bean.getNextCondition());

        long cur = database.insert(TABLE_NAME,null,values);
        Log.d(TAG,"addCityComplete cur: " + cur);
        return cur;
    }

    public static void addCity(String cityCode){
        ContentValues values = new ContentValues();
        values.put("cityCode",cityCode);
        long cur = database.insert(TABLE_NAME,null,values);
        Log.d(TAG,"addCity cur: " + cur);
    }

    //删除城市
    public static void deleteCityByCode(String cityCode){
        int count = database.delete(TABLE_NAME,"cityCode=?",new String[]{cityCode});
        Log.d(TAG,"deleteCityByCode count: " + count);
    }
    public static void deleteCityByName(String cityName){
        int count = database.delete(TABLE_NAME,"cityName=?",new String[]{cityName});
        Log.d(TAG,"deleteCityByName count: " + count);
    }

    //根据城市代码，更新数据
    public static void updateCityByCode(String cityCode, DataBaseBean bean){
        ContentValues values = new ContentValues();
        values.put("cityName",bean.getCityName());
        values.put("condition",bean.getCondition());
        values.put("curTemp",bean.getCurTemp());
        values.put("dressIndex",bean.getDressIndex());
        values.put("sunIndex",bean.getSunIndex());
        values.put("coldIndex",bean.getColdIndex());
        values.put("umbrellaIndex",bean.getUmbrellaIndex());
        values.put("carIndex",bean.getCarIndex());
        values.put("exerciseIndex",bean.getExerciseIndex());
        values.put("todayIcon",bean.getTodayIcon());
        values.put("todayMinTemp",bean.getTodayMinTemp());
        values.put("todayMaxTemp",bean.getTodayMaxTemp());
        values.put("todayCondition",bean.getTodayCondition());
        values.put("tomorrowIcon",bean.getTomorrowIcon());
        values.put("tomorrowMinTemp",bean.getTomorrowMinTemp());
        values.put("tomorrowMaxTemp",bean.getTomorrowMaxTemp());
        values.put("tomorrowCondition",bean.getTomorrowCondition());
        values.put("nextIcon",bean.getNextIcon());
        values.put("nextMinTemp",bean.getNextMinTemp());
        values.put("nextMaxTemp",bean.getNextMaxTemp());
        values.put("nextCondition",bean.getNextCondition());

        int count = database.update(TABLE_NAME,values,"cityCode=?", new String[]{cityCode});
        Log.d(TAG,"updateCityByCode count: " + count);
    }

    //根据城市代码查找信息
    public static DataBaseBean queryByCityCode(String cityCode){
        Cursor cursor = database.query(TABLE_NAME,null, "cityCode=?", new String[]{cityCode},null, null, null);
        DataBaseBean bean = new DataBaseBean();
        bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
        bean.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
        bean.setCityCode(cityCode);
        bean.setCurTemp(cursor.getString(cursor.getColumnIndex("curTemp")));
        bean.setCondition(cursor.getString(cursor.getColumnIndex("condition")));
        bean.setDressIndex(cursor.getString(cursor.getColumnIndex("dressIndex")));
        bean.setColdIndex(cursor.getString(cursor.getColumnIndex("coldIndex")));
        bean.setSunIndex(cursor.getString(cursor.getColumnIndex("sunIndex")));
        bean.setCarIndex(cursor.getString(cursor.getColumnIndex("carIndex")));
        bean.setExerciseIndex(cursor.getString(cursor.getColumnIndex("exerciseIndex")));
        bean.setUmbrellaIndex(cursor.getString(cursor.getColumnIndex("umbrellaIndex")));
        bean.setTodayIcon(cursor.getString(cursor.getColumnIndex("todayIcon")));
        bean.setTodayCondition(cursor.getString(cursor.getColumnIndex("todayCondition")));
        bean.setTodayMinTemp(cursor.getString(cursor.getColumnIndex("todayMinTemp")));
        bean.setTodayMaxTemp(cursor.getString(cursor.getColumnIndex("todayMaxTemp")));
        bean.setTomorrowIcon(cursor.getString(cursor.getColumnIndex("tomorrowIcon")));
        bean.setTomorrowCondition(cursor.getString(cursor.getColumnIndex("tomorrowCondition")));
        bean.setTomorrowMinTemp(cursor.getString(cursor.getColumnIndex("tomorrowMinTemp")));
        bean.setTomorrowMaxTemp(cursor.getString(cursor.getColumnIndex("tomorrowMaxTemp")));
        bean.setNextIcon(cursor.getString(cursor.getColumnIndex("nextIcon")));
        bean.setNextCondition(cursor.getString(cursor.getColumnIndex("nextCondition")));
        bean.setNextMinTemp(cursor.getString(cursor.getColumnIndex("nextMinTemp")));
        bean.setNextMaxTemp(cursor.getString(cursor.getColumnIndex("nextMaxTemp")));
        return bean;
    }

    //存储城市天气最多存储五个
    public static int getCityCount(){
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        int count = cursor.getCount();
        return count;
    }
}
