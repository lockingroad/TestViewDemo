package com.example.zhihu.testviewdemo.sticknav;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

import com.example.zhihu.testviewdemo.BaseRecyclerFragment;
import com.example.zhihu.testviewdemo.R;
import com.example.zhihu.testviewdemo.temp.StaticFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-21-2019
 */
public class StickNavActivity2 extends AppCompatActivity {
    private MixtapeVideoView mMixtapeVideoView;
//    private View mMidView;
    private ViewPager mViewPager;
    private SticktPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_nav);
        mMixtapeVideoView = findViewById(R.id.stick_video);
//        mMidView = findViewById(R.id.stick_mid);
        mViewPager = findViewById(R.id.stick_vp);
        mTabLayout = findViewById(R.id.stick_tab);

        List<PageItem> pageItems = new ArrayList<>();
        pageItems.add(new PageItem(BaseRecyclerFragment.getInstance("目录"), "目录"));
        pageItems.add(new PageItem(BaseRecyclerFragment.getInstance("评论"), "评论"));
        mAdapter = new SticktPagerAdapter(getSupportFragmentManager(), pageItems);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mAdapter);

    }
}
