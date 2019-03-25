package com.example.zhihu.testviewdemo.sticknav;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-21-2019
 */
public class SticktPagerAdapter extends FragmentPagerAdapter {
    List<PageItem> mItems;

    public SticktPagerAdapter(FragmentManager fm, List<PageItem> items) {
        super(fm);
        mItems = items;
    }

    @Override
    public Fragment getItem(int i) {
        return mItems.get(i).mFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }
}
