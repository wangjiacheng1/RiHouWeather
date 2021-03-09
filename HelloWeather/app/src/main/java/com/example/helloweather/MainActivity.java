package com.example.helloweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView addCityIv;
    ImageView moreIv;
    LinearLayout pointLayout;
    ViewPager mainViewPager;
    //viewPager的数据源
    List<Fragment> fragmentList;
    List<String> cityList;
    //viewPager的页数指示器集合
    List<ImageView> imgList;

    CityFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //为了美观隐藏toolbar
        getSupportActionBar().hide();

        initView();
        fragmentList = new ArrayList<>();
        cityList = new ArrayList<>();
        imgList = new ArrayList<>();

        if (cityList.size() == 0){
            cityList.add("CN101010100");
        }
        //初始化viewPager页面
        initPager();
        adapter = new CityFragmentAdapter(getSupportFragmentManager(), fragmentList);
        mainViewPager.setAdapter(adapter);
        //创建页面小圆点指示器
        initPoint();

    }

    private void initView(){
        addCityIv = findViewById(R.id.main_iv_add);
        moreIv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        mainViewPager = findViewById(R.id.main_vp);

        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_iv_add:
                break;
            case R.id.main_iv_more:
                break;
        }
    }

    public void initPager(){
        //创建Fragment对象，添加到viewPager数据源
        for (int i = 0; i < cityList.size(); i++) {
            CityWeatherFragment cityWeatherFragment = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cityWeatherFragment.setArguments(bundle);
            fragmentList.add(cityWeatherFragment);
        }
    }

    public void initPoint(){

    }
}