package com.example.zhihu.testviewdemo.landscape;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.zhihu.testviewdemo.R;

/**
 * @author liuran @ Zhihu Inc.
 * @since 04-04-2019
 */
public class LandScapeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mPBtn, mLBtn;
    String TAG = "LandScapeActivity";
    private FrameLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);
        mPBtn = findViewById(R.id.portrait_btn);
        mLBtn = findViewById(R.id.landscape_btn);
        mContainer = findViewById(R.id.container);
        mPBtn.setOnClickListener(this);
        mLBtn.setOnClickListener(this);
        Log.e(TAG, "onCreate: " + (savedInstanceState == null));
//        initView();
    }

    private void initView() {
        PlayControlView view = new PlayControlView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(view, lp);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.landscape_btn:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                break;
            case R.id.portrait_btn:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                break;
        }
    }
}
