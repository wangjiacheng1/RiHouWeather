package com.example.helloweather;

import android.app.Application;

import com.example.helloweather.database.DBHelper;
import com.example.helloweather.database.DBManager;
import com.qweather.sdk.view.HeConfig;

import org.xutils.x;

public class UniteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //初始化sdk
        HeConfig.init("HE2103071436381317","90b7b38c6f0742ccbbabc7105d6425e5");
        HeConfig.switchToDevService();

        //初始化数据库
        DBManager.initDB(this);
    }
}
