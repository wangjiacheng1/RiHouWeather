package com.example.helloweather;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.helloweather.database.DBManager;
import com.example.helloweather.database.DataBaseBean;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.geo.GeoBean;
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

    DataBaseBean bean = new DataBaseBean();
    Context context;
    String cityCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        context = getContext();
        //通过activity传值当前城市给fragment
        Bundle bundle = getArguments();
        String cityName = bundle.getString("city","北京");
        bean.setCityName(cityName);


        getCityCode(cityName);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //更新数据
    public void updateData(String cityCode){
        //获得并解析数据

        QWeather.getWeatherNow(context, cityCode, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getNowWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == weatherBean.getCode()) {
                    WeatherNowBean.NowBaseBean now = weatherBean.getNow();
                    bean.setCurTemp(now.getTemp());
                    bean.setCondition(now.getText());
                    //获取未来三天天气
                    getWeather3D(cityCode);
                } else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(weatherBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);

                }
            }

        });
    }

    /**
     * 获取未来三天天气
     */
    private void getWeather3D(String cityCode){
        QWeather.getWeather3D(context, cityCode, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "get3DWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherDailyBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == weatherDailyBean.getCode()) {

                    WeatherDailyBean.DailyBean today = weatherDailyBean.getDaily().get(0);
                    WeatherDailyBean.DailyBean tomorrow = weatherDailyBean.getDaily().get(1);
                    WeatherDailyBean.DailyBean next = weatherDailyBean.getDaily().get(2);

                    bean.setTodayIcon(today.getIconDay());
                    bean.setTodayMinTemp(today.getTempMin());
                    bean.setTodayMaxTemp(today.getTempMax());
                    bean.setTodayCondition(today.getTextDay());
                    bean.setTomorrowIcon(tomorrow.getIconDay());
                    bean.setTomorrowMinTemp(tomorrow.getTempMin());
                    bean.setTomorrowMaxTemp(tomorrow.getTempMax());
                    bean.setTomorrowCondition(tomorrow.getTextDay());
                    bean.setNextIcon(next.getIconDay());
                    bean.setNextMinTemp(next.getTempMin());
                    bean.setNextMaxTemp(next.getTempMax());
                    bean.setNextCondition(next.getTextDay());
                    //展示数据
                    showDate(cityCode);
                    //存进数据库
                    updateCity(cityCode);

                } else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(weatherDailyBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);

                }

            }
        });

    }
    //展示数据
    public void showDate(String cityCode){
        context = getContext();
        ApplicationInfo info = context.getApplicationInfo();
//        DataBaseBean cityShow = DBManager.queryByCityCode(cityCode);
        Log.d(TAG,"showDate: "+bean.toString());

        cityTv.setText(bean.getCityName());
        tempTv.setText(bean.getCurTemp());
        conditionTv.setText(bean.getCondition());

        todayMinTv.setText(bean.getTodayMinTemp() + "℃");
        todayMaxTv.setText(bean.getTodayMaxTemp() + "℃");
        todayTextTv.setText("今天 · " + bean.getTodayCondition());
        todayIcon.setImageResource(getResources().getIdentifier("i" + bean.getTodayIcon(),"mipmap", info.packageName));
        tomorrowMinTv.setText(bean.getTomorrowMinTemp() + "℃");
        tomorrowMaxTv.setText(bean.getTomorrowMaxTemp() + "℃");
        tomorrowTextTv.setText("明天 · " + bean.getTomorrowCondition());
        tomorrowIcon.setImageResource(getResources().getIdentifier("i" + bean.getTomorrowIcon(),"mipmap", info.packageName));
        nextMinTv.setText(bean.getNextMinTemp() + "℃");
        nextMaxTv.setText(bean.getNextMaxTemp() + "℃");
        nextTextTv.setText("后天 · " + bean.getNextCondition());
        nextIcon.setImageResource(getResources().getIdentifier("i" + bean.getNextIcon(),"mipmap", info.packageName));
    }

    //解析城市
    public void getCityCode(String cityName){
        context = getContext();
        final String[] cityCode = new String[1];
        QWeather.getGeoCityLookup(context, cityName, new QWeather.OnResultGeoListener(){
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getLocation onError: " + e);
            }
            @Override
            public void onSuccess(GeoBean geoBean){
                if (Code.OK == geoBean.getCode()){
                    String cityCode = geoBean.getLocationBean().get(0).getId();
                    Log.d(TAG,"getCityCode: " + cityCode);

                    bean.setCityCode(cityCode);
                    //获取当前天气
                    updateData(cityCode);
                }else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(geoBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
        Log.d(TAG,"cityCode at out:" + cityCode[0]);
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

    public void updateCity(String cityCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager.updateCityByCode(cityCode, bean);
            }
        }).start();

    }
}