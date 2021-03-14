package com.example.helloweather.city_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.helloweather.R;
import com.example.helloweather.database.DBManager;
import com.example.helloweather.database.DataBaseBean;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backIv, deleteIv;
    SearchView searchView;
    ListView listView;
    List<DataBaseBean> mDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        //隐藏toolbar
        getSupportActionBar().hide();

        initView();
        mDates = new ArrayList<>();
        //设置设配器


    }
    public void initView(){
        backIv.findViewById(R.id.search_iv_back);
        deleteIv.findViewById(R.id.search_iv_del);
        searchView.findViewById(R.id.search_searchSv);
        listView.findViewById(R.id.search_Lv);

        backIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        searchView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_del:
                startActivity(new Intent(this, DeleteCityActivity.class));
                break;
            case R.id.search_searchSv:
                int cityCount = DBManager.getCityCount();
                if (cityCount < 5){

                }else {
                    Toast.makeText(this,"存储数量已达上限：5，请删除后再添加",Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(this, SearchCityActivity.class));
                break;
        }
    }
}