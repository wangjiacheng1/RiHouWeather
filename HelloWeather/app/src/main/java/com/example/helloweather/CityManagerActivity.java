package com.example.helloweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {

    SearchView searchView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        //隐藏toolbar
        getSupportActionBar().hide();

        initView();

    }
    public void initView(){
        searchView.findViewById(R.id.search_searchSv);
        listView.findViewById(R.id.search_Lv);

        searchView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}