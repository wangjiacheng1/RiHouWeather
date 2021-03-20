package com.example.helloweather.city_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helloweather.R;
import com.example.helloweather.database.DBManager;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView backIv, deleteIv;
    ListView deleteLv;

    List<String> mData;            //listView数据源
    List<String> deleteCities;     //表示要删除的数据信息
    DeleteCityAdapter adapter;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        mContext = getApplicationContext();
        getSupportActionBar().hide();
        initView();
        mData = new ArrayList<>();
        deleteCities = new ArrayList<>();
        //设置设配器
        adapter = new DeleteCityAdapter(mContext,mData, deleteCities);
        deleteLv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> list = DBManager.queryAllCityName();
        mData.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void initView(){
        backIv = findViewById(R.id.delete_iv_back);
        deleteIv = findViewById(R.id.delete_iv_enter);
        deleteLv = findViewById(R.id.delete_lv);

        backIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_iv_back:
                if (deleteCities.size() != 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示信息").setMessage("确定要放弃更改么?").setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭当前activity
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.create().show();
                }else {
                    finish();
                }
                break;
            case R.id.delete_iv_enter:
                if (deleteCities.size() != 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示信息").setMessage("确定要保存当前更改么?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCityByAsync();
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.create().show();
                }else {
                    Toast.makeText(mContext,"您没有任何更改",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void deleteCityByAsync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < deleteCities.size(); i++){
                    DBManager.deleteCityByName(deleteCities.get(i));
                }
            }
        }).start();
    }
}