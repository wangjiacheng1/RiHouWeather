package com.example.helloweather.city_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.helloweather.R;
import com.example.helloweather.database.DataBaseBean;

import java.util.List;

public class CityManagerAdapter extends BaseAdapter {
    Context context;
    List<DataBaseBean> mDatas;

    public CityManagerAdapter(Context context, List<DataBaseBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.city_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        DataBaseBean bean = mDatas.get(position);
        holder.cityTv.setText(bean.getCityName());
        holder.conditionTv.setText(bean.getCondition());
        holder.currentTempTv.setText(bean.getCurTemp() + "°");
        holder.tempTv.setText(bean.getTodayMinTemp() + "℃ / " + bean.getTodayMaxTemp() + "℃");
        return convertView;
    }

    class ViewHolder{
        TextView cityTv, conditionTv, tempTv, currentTempTv;

        public ViewHolder(View itemView) {
            this.cityTv = itemView.findViewById(R.id.item_cityTv);
            this.conditionTv = itemView.findViewById(R.id.item_conditionTv);
            this.tempTv = itemView.findViewById(R.id.item_minAndMaxTempTv);
            this.currentTempTv = itemView.findViewById(R.id.item_currentTempTv);
        }
    }
}
