package com.example.zhihu.testviewdemo.sticknav;

import android.support.v4.app.Fragment;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-21-2019
 */
public class PageItem {

    public Fragment mFragment;

    public String title;

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PageItem(Fragment fragment, String title) {
        mFragment = fragment;
        this.title = title;
    }
}
