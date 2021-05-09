package com.example.helloweather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class CityFragmentAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragmentList;

    public CityFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * childCount表示viewPager包含的页数
     * 页数发生改变时，必须重写两个函数
     */
    int childCount = 0;

    @Override
    public void notifyDataSetChanged() {
        this.childCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (childCount > 0){
            childCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
