package com.example.helloweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.helloweather.city_manager.CityManagerActivity;
import com.example.helloweather.database.DBManager;

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
    Intent mIntent;
    Context mContext;

    MyTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        //为了美观隐藏toolbar
        getSupportActionBar().hide();

        initView();
        fragmentList = new ArrayList<>();
        mTask = new MyTask();
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        cityList = DBManager.queryAllCityName();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //获取数据库中的还剩的城市
        List<String> curCities = DBManager.queryAllCityName();
        if (curCities.size() == 0){
            curCities.add("北京");
            DBManager.addCity("CN101010100");
        }
        //重新加载前清空原数据源
        cityList.clear();
        cityList.addAll(curCities);
        //剩余城市创建对应的fragment
        fragmentList.clear();
        initPager();

        adapter.notifyDataSetChanged();
        //页面数量改变，指示器的数量也会发生变化，重写设置指示器
        imgList.clear();
        pointLayout.removeAllViews(); //将原布局中的元素清除
        initPoint();
        mainViewPager.setCurrentItem(fragmentList.size() - 1);
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
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.main_iv_add:
                intent.setClass(this, CityManagerActivity.class);
                break;
//            case R.id.main_iv_more:
//                break;
        }
        startActivity(intent);
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
        //创建小圆点，ViewPager页面指示器
        for (int i = 0;i < fragmentList.size(); i++){
            ImageView pointIv = new ImageView(this);
            pointIv.setImageResource(R.mipmap.point0);
            pointIv.setLayoutParams(new LinearLayout.LayoutParams(80,80));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) pointIv.getLayoutParams();
            layoutParams.setMargins(0,0,2,0);
            imgList.add(pointIv);
            pointLayout.addView(pointIv);
        }
        imgList.get(imgList.size() -1).setImageResource(R.mipmap.point1);
    }

    public void initPagerListener(){
        mainViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i =0; i < imgList.size(); i++){
                    imgList.get(i).setImageResource(R.mipmap.point0);
                }
                imgList.get(position).setImageResource(R.mipmap.point1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class MyTask extends AsyncTask<Void, Integer, List<String>>{

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> cityNameList = DBManager.queryAllCityName();
            return cityNameList;
        }

        @Override
        protected void onPostExecute(List<String> list){
            super.onPostExecute(list);
            cityList = list;

            if (cityList.size() == 0){
                cityList.add("北京");
                DBManager.addCity("CN101010100");
                cityList.add("吉林");
                DBManager.addCity("CN101060201");
                cityList.add("石家庄");
                DBManager.addCity("CN101090101");
            }
            //获取搜索界面传来的城市
            mIntent = getIntent();
            String searchCityName = mIntent.getStringExtra("searchCityName");
            String searchCityCode = mIntent.getStringExtra("searchCityCode");
            if (null != searchCityName){
                if (!cityList.contains(searchCityName)){
                    cityList.add(searchCityName);
                    DBManager.addCity(searchCityCode);
                }else {
                    Toast.makeText(mContext,"搜索城市已存在",Toast.LENGTH_SHORT).show();
                }
            }

            imgList = new ArrayList<>();

            //初始化viewPager页面
            initPager();
            adapter = new CityFragmentAdapter(getSupportFragmentManager(), fragmentList);
            mainViewPager.setAdapter(adapter);
            //创建页面小圆点指示器
            initPoint();
            //设置最后一个城市信息
            mainViewPager.setCurrentItem(fragmentList.size() -1);
            //设置viewpager监听
            initPagerListener();
        }
    }

}