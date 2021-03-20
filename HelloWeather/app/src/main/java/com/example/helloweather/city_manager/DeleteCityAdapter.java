package com.example.helloweather.city_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helloweather.R;

import java.util.List;

public class DeleteCityAdapter extends BaseAdapter {
    Context mContext;
    List<String> mData;
    List<String> deleteCities;

    public DeleteCityAdapter(Context mContext, List<String> mData, List<String> deleteCities) {
        this.mContext = mContext;
        this.mData = mData;
        this.deleteCities = deleteCities;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.delete_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String city = mData.get(position);
        holder.tv.setText(city);
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(city);
                deleteCities.add(city);
                //删除后提示设配器更新
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
        public ViewHolder(View itemView){
            tv = itemView.findViewById(R.id.item_delete_tv);
            iv = itemView.findViewById(R.id.item_delete_iv);
        }
    }
}
