 package com.example.helloweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.helloweather.bean.IndexBean;
import com.example.helloweather.database.DBManager;
import com.example.helloweather.database.DataBaseBean;
import com.google.gson.Gson;
import com.qweather.sdk.bean.IndicesBean;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.IndicesType;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    List<IndicesType> types = new ArrayList<>();
    List<IndexBean> indexBeans = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        initView(view);
        context = getContext();
        //为生活指数添加参数
        setIndexType();
        //通过activity传值当前城市给fragment
        Bundle bundle = getArguments();
        String cityName = bundle.getString("city","北京");
        bean.setCityName(cityName);


        getCityCode(cityName);


        return view;
    }

    private void setIndexType(){
        //穿衣指数
        types.add(IndicesType.DRSG);
        //防晒指数
        types.add(IndicesType.SPI);
        //感冒指数
        types.add(IndicesType.FLU);
        //洗车指数
        types.add(IndicesType.CW);
        //运动指数
        types.add(IndicesType.SPT);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获得实时天气数据
     * @param cityCode
     */
    public void getNowWeather(Context context, String cityCode){
        //获得并解析数据

        QWeather.getWeatherNow(context, cityCode, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "getNowWeather onError: " + e);
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
                    getWeather3D(context, cityCode);
                } else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(weatherBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.e(TAG, "failed code: " + code);
                }
            }

        });
    }

    /**
     * 获取未来三天天气
     */
    private void getWeather3D(Context mContext, String cityCode){
        QWeather.getWeather3D(context, cityCode, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "get3DWeather onError: " + e);
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
                    showDate(mContext, cityCode);
                    //存进数据库
                    updateCity(cityCode);

                } else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(weatherDailyBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.e(TAG, "failed code: " + code);

                }

            }
        });

    }

    /**
     * 获取生活指数
     * @param mContext
     * @param cityCode
     */
    private void getIndex(Context mContext, String cityCode){
        QWeather.getIndices1D(mContext, cityCode, Lang.ZH_HANS, types, new QWeather.OnResultIndicesListener(){

            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG, "getIndices1D onError: " + throwable);
            }

            @Override
            public void onSuccess(IndicesBean indicesBean) {
                Log.i(TAG, "getIndices1D onSuccess: " + new Gson().toJson(indicesBean));
                if (Code.OK == indicesBean.getCode()){
                    for(IndicesBean.DailyBean dailyBean : indicesBean.getDailyList()){
                        IndexBean index = new IndexBean();
                        index.setName(dailyBean.getName());
                        index.setDate(dailyBean.getDate());
                        index.setLevel(dailyBean.getLevel());
                        index.setText(dailyBean.getText());
                        indexBeans.add(index);
                    }
                    bean.setDressIndex(indicesBean.getDailyList().get(1).getLevel());
                    bean.setSunIndex(indicesBean.getDailyList().get(2).getLevel());
                    bean.setColdIndex(indicesBean.getDailyList().get(4).getLevel());
                    bean.setCarIndex(indicesBean.getDailyList().get(0).getLevel());
                    bean.setExerciseIndex(indicesBean.getDailyList().get(3).getLevel());
                }else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(indicesBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.e(TAG, "failed code: " + code);
                }
            }
        });

    }

    //展示数据
    public void showDate(Context mContext, String cityCode){
//        Context mcontext = getContext();
        ApplicationInfo info = mContext.getApplicationInfo();
        Log.d(TAG,"context in showDate = " + mContext);
        Log.d(TAG,"info = " + info);
//        DataBaseBean cityShow = DBManager.queryByCityCode(cityCode);
        Log.d(TAG,"showDate: "+bean.toString());

        if (isAdded()) {
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
        if(bean.getDressIndex() != null){
            showIndex();
        }

    }

    //展示生活指数
    public void showIndex(){
        Log.d(TAG,"穿衣指数：" + bean.getDressIndex());
        //穿衣指数
        if (Integer.parseInt(bean.getDressIndex()) <= 2){
            dressIndexTv.setText("适宜棉衣");
        }else if(Integer.parseInt(bean.getDressIndex()) > 2 && Integer.parseInt(bean.getDressIndex()) <= 5){
            dressIndexTv.setText("需穿外套");
        }else if(Integer.parseInt(bean.getDressIndex()) > 5){
            dressIndexTv.setText("适宜单衣");
        }
        //防晒指数
        if(Integer.parseInt(bean.getSunIndex()) <= 2){
            sunIndexTv.setText("无需防晒");
        }else if (Integer.parseInt(bean.getSunIndex()) > 2 && Integer.parseInt(bean.getSunIndex()) <= 4){
            sunIndexTv.setText("需要防晒");
        }else if (Integer.parseInt(bean.getSunIndex()) > 4){
            sunIndexTv.setText("加强防晒");
        }
        //感冒指数
        if (Integer.parseInt(bean.getColdIndex()) <= 2){
            coldIndexTv.setText("不易感冒");
        }else if (Integer.parseInt(bean.getColdIndex()) > 2){
            coldIndexTv.setText("容易感冒");
        }
        //洗车指数
        if (Integer.parseInt(bean.getCarIndex()) <= 2){
            carIndexTv.setText("适宜洗车");
        }else if (Integer.parseInt(bean.getCarIndex()) > 2){
            carIndexTv.setText("不宜洗车");
        }
        //运动指数
        if (Integer.parseInt(bean.getExerciseIndex()) <= 2){
            exerciseIndexTv.setText("适宜运动");
        }else if (Integer.parseInt(bean.getExerciseIndex()) > 2){
            exerciseIndexTv.setText("不宜运动");
        }
    }

    //解析城市
    public void getCityCode(String cityName){
        context = getContext();
        Log.d(TAG,"CONTEXT = " + context);
        final String[] cityCode = new String[1];
        QWeather.getGeoCityLookup(context, cityName, new QWeather.OnResultGeoListener(){
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getLocation onError: " + e);
            }
            @Override
            public void onSuccess(GeoBean geoBean){
                if (Code.OK == geoBean.getCode()){
                    String cityCode = "CN" + geoBean.getLocationBean().get(0).getId();
                    Log.d(TAG,"getCityCode: " + cityCode);

                    bean.setCityCode(cityCode);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getIndex(context,cityCode);
                        }
                    }).start();
                    //获取当前天气
                    getNowWeather(context, cityCode);
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
        //设置点击事件
        dressIndexTv.setOnClickListener(this);
        sunIndexTv.setOnClickListener(this);
        coldIndexTv.setOnClickListener(this);
        umbrellaIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        exerciseIndexTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String msg = null;
        switch (v.getId()){
            case R.id.frag_index_dress:
                builder.setTitle("穿衣指数");
                msg = indexBeans.get(1).getText();
                builder.setMessage(msg);
                break;
            case R.id.frag_index_sun:
                builder.setTitle("防晒指数");
                msg = indexBeans.get(2).getText();
                builder.setMessage(msg);
                break;
            case R.id.frag_index_cold:
                builder.setTitle("感冒指数");
                msg = indexBeans.get(4).getText();
                builder.setMessage(msg);
                break;
            case R.id.frag_index_umbrella:
                builder.setTitle("降雨指数");
                msg = "短时间不会下雨，放心出门吧";
                builder.setMessage(msg);
                break;
            case R.id.frag_index_washCar:
                builder.setTitle("洗车指数");
                msg = indexBeans.get(0).getText();
                builder.setMessage(msg);
                break;
            case R.id.frag_index_exercise:
                builder.setTitle("运动指数");
                msg = indexBeans.get(3).getText();
                builder.setMessage(msg);
                break;
        }
        builder.create().show();
    }

    public void updateCity(final String cityCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBManager.updateCityByCode(cityCode, bean);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}