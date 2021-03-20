package com.example.helloweather.city_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloweather.MainActivity;
import com.example.helloweather.R;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.QWeather;

import java.util.List;

public class SearchCityActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchCityActivity";

    EditText searchEv;
    ImageView submitIv;
    GridView searchGv;

    private String[] hotCities = new String[10];
    private ArrayAdapter<String> adapter;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        mContext = getApplicationContext();
        //隐藏toolbar
        getSupportActionBar().hide();
        initView();
        //加载热门城市
        getHotCity(mContext);
        setListener();
    }

    public void initView(){
        searchEv = findViewById(R.id.search_et);
        submitIv = findViewById(R.id.search_iv_submit);
        searchGv = findViewById(R.id.search_gv);

        submitIv.setOnClickListener(this);
        searchEv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    submitIv.performClick();
                }
                return false;
            }
        });
    }

    //设置GridView监听
    public void setListener(){
        searchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = hotCities[position];
                searCity(mContext,city);
            }
        });
    }

    public void getHotCity(Context context){
        QWeather.getGeoTopCity(context, new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "getGeoTopCity onError: " + throwable);
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                Log.i(TAG, "getGeoTopCity onSuccess: " + new Gson().toJson(geoBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == geoBean.getCode()) {
                    List<GeoBean.LocationBean> list = geoBean.getLocationBean();
                    for (int i = 0; i < hotCities.length; i++){
                        hotCities[i] = list.get(i).getName();
                    }
                    //设置设配器
                    setHotCity(hotCities);
                } else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(geoBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }

    public void setHotCity(String[] cities){
        adapter = new ArrayAdapter<>(this,R.layout.hotcity_item,cities);
        searchGv.setAdapter(adapter);
    }

    public void searCity(final Context context, String cityName){
        QWeather.getGeoCityLookup(context, cityName, new QWeather.OnResultGeoListener(){
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getLocation onError: " + e);
                Toast.makeText(context,"暂未收入此城市天气信息...",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(GeoBean geoBean){
                if (Code.OK == geoBean.getCode()){
                    String cityCode = "CN" + geoBean.getLocationBean().get(0).getId();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("searchCityName",cityName);
                    intent.putExtra("searchCityCode",cityCode);
                    startActivity(intent);
                }else {
                    //在此查看返回数据失败的原因
                    String status = String.valueOf(geoBean.getCode());
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                    Toast.makeText(context,"暂未收入此城市天气信息...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_iv_submit:
                String city = searchEv.getText().toString();
                if (!TextUtils.isEmpty(city)){
                    searCity(mContext,city);
                }else {
                    Toast.makeText(mContext,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                }
        }
    }
}