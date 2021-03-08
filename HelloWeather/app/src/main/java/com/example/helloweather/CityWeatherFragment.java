package com.example.helloweather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.helloweather.bean.CurrentMsg;
import com.google.gson.Gson;

public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {

    TextView cityTv, tempTv, conditionTv, todayTextTv, tomorrowTextTv, nextTomorrowTextTv,
            todayMinTv, todayMaxTv, tomorrowMinTv, tomorrowMaxTv, nextMinTv, nextMaxTv;
    TextView dressIndexTv, sunIndexTv, coldIndexTv, umbrellaIndexTv, carIndexTv, exerciseIndexTv;
    ImageView todayIcon, tomorrowIcon, nextIcon;
    RelativeLayout todayLayout, tomorrowLayout, nextLayout;

    String url1 = "https://devapi.qweather.com/v7/weather/now?location=";
    String url2 = "&key=f1f0fb53e53542a59a6a1cb9081b59bb";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        //通过activity传值当前城市给fragment
        Bundle bundle = getArguments();
        String city = bundle.getString("city");
        String url = url1 + city + url2;
        //调用父类获取数据的方法
        loadData(url);
        return view;
    }

    //重写父类获取数据的方法
    @Override
    public void onSuccess(String result) {
        //解析并展示数据
        parseShowData(result);
    }
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
    }

    //解析展示数据
    public void parseShowData(String result){
        //使用Gson解析数据
        CurrentMsg currentMsg = new Gson().fromJson(result, CurrentMsg.class);
        String currentTemp = currentMsg.getNow().getTemp();
        String condition = currentMsg.getNow().getText();
        tempTv.setText(currentTemp);
        conditionTv.setText(condition);
    }

    private void initView(View view){
        //初始化控件
        cityTv = view.findViewById(R.id.frag_tv_city);
        tempTv = view.findViewById(R.id.frag_tv_currentTemp);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        todayTextTv = view.findViewById(R.id.frag_tv_today_text);
        tomorrowTextTv = view.findViewById(R.id.frag_tv_tomorrow_text);
        nextTomorrowTextTv = view.findViewById(R.id.frag_tv_nextTomorrow_text);
        todayMinTv = view.findViewById(R.id.frag_tv_today_min);
        todayMaxTv = view.findViewById(R.id.frag_tv_today_max);
        tomorrowMinTv = view.findViewById(R.id.frag_tv_tomorrow_min);
        tomorrowMaxTv = view.findViewById(R.id.frag_tv_tomorrow_max);
        nextMinTv = view.findViewById(R.id.frag_tv_nextTomorrow_min);
        nextMaxTv = view.findViewById(R.id.frag_tv_nextTomorrow_max);
        dressIndexTv = view.findViewById(R.id.frag_index_dress);
        sunIndexTv = view.findViewById(R.id.frag_index_sun);
        coldIndexTv = view.findViewById(R.id.frag_index_cold);
        umbrellaIndexTv = view.findViewById(R.id.frag_index_umbrella);
        carIndexTv = view.findViewById(R.id.frag_index_washCar);
        exerciseIndexTv = view.findViewById(R.id.frag_index_exercise);

        todayIcon = view.findViewById(R.id.frag_iv_today_icon);
        tomorrowIcon = view.findViewById(R.id.frag_iv_tomorrow_icon);
        nextIcon = view.findViewById(R.id.frag_iv_nextTomorrow_icon);

        todayLayout = view.findViewById(R.id.frag_layout_today);
        tomorrowLayout = view.findViewById(R.id.frag_layout_tomorrow);
        nextLayout = view.findViewById(R.id.frag_layout_nextTomorrow);
        //设置点击事件，跳转到每天详情页
        todayLayout.setOnClickListener(this);
        tomorrowLayout.setOnClickListener(this);
        nextLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frag_layout_today:
                break;
            case R.id.frag_layout_tomorrow:
                break;
            case R.id.frag_layout_nextTomorrow:
                break;
        }
    }
}