package cn.weipan.fb.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class InOrderViewPagerAdapter extends PagerAdapter {

    private List<View> mListViews;
    private String[] titleList;

    public InOrderViewPagerAdapter(List<View> mListViews) {
        this.mListViews = mListViews;
    }

    public void setTitleList(String[] titleList) {
        this.titleList = titleList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position), 0);
        return mListViews.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position % titleList.length].toUpperCase();
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}