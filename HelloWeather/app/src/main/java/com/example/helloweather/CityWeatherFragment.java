package com.example.helloweather;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

public class CityWeatherFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "CityWeatherFragment";

    TextView cityTv, tempTv, conditionTv, todayTextTv, tomorrowTextTv, nextTextTv,
            todayMinTv, todayMaxTv, tomorrowMinTv, tomorrowMaxTv, nextMinTv, nextMaxTv;
    TextView dressIndexTv, sunIndexTv, coldIndexTv, umbrellaIndexTv, carIndexTv, exerciseIndexTv;
    ImageView todayIcon, tomorrowIcon, nextIcon;
    RelativeLayout todayLayout, tomorrowLayout, nextLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        //通过activity传值当前城市给fragment
        Bundle bundle = getArguments();
        String city = bundle.getString("city","CN101010100");

        parseShowData(city);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        String city = bundle.getString("city","CN101010100");
        parseShowData(city);
    }

    //解析展示数据
    public void parseShowData(String city){
        //获得并解析数据
        Context context = getContext();
        ApplicationInfo info = context.getApplicationInfo();
        QWeather.getWeatherNow(context, city, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == weatherBean.getCode()) {
                    WeatherNowBean.NowBaseBean now = weatherBean.getNow();
                    String currentTemp = now.getTemp();
                    String condition = now.getText();
                    tempTv.setText(currentTemp);
                    conditionTv.setText(condition);
                } else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(weatherBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);

                }
            }

        });
        QWeather.getWeather3D(context, city, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherDailyBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == weatherDailyBean.getCode()) {

                    WeatherDailyBean.DailyBean today = weatherDailyBean.getDaily().get(0);
                    WeatherDailyBean.DailyBean tomorrow = weatherDailyBean.getDaily().get(1);
                    WeatherDailyBean.DailyBean next = weatherDailyBean.getDaily().get(2);
                    todayMinTv.setText(today.getTempMin() + "℃");
                    todayMaxTv.setText(today.getTempMax() + "℃");
                    todayTextTv.setText("今天 · " + today.getTextDay());
                    todayIcon.setImageResource(getResources().getIdentifier("i" + today.getIconDay(),"mipmap", info.packageName));
                    tomorrowMinTv.setText(tomorrow.getTempMin() + "℃");
                    tomorrowMaxTv.setText(tomorrow.getTempMax() + "℃");
                    tomorrowTextTv.setText(tomorrow.getTextDay());
                    tomorrowIcon.setImageResource(getResources().getIdentifier("i" + tomorrow.getIconDay(),"mipmap", info.packageName));
                    nextMinTv.setText(next.getTempMin() + "℃");
                    nextMaxTv.setText(next.getTempMax() + "℃");
                    nextTextTv.setText(next.getTextDay());
                    nextIcon.setImageResource(getResources().getIdentifier("i" + next.getIconDay(),"mipmap", info.packageName));
                } else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(weatherDailyBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);

                }

            }
        });
    }

    private void initView(View view){
        //初始化控件
        cityTv = view.findViewById(R.id.frag_tv_city);
        tempTv = view.findViewById(R.id.frag_tv_currentTemp);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        todayTextTv = view.findViewById(R.id.frag_tv_today_text);
        tomorrowTextTv = view.findViewById(R.id.frag_tv_tomorrow_text);
        nextTextTv = view.findViewById(R.id.frag_tv_nextTomorrow_text);
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