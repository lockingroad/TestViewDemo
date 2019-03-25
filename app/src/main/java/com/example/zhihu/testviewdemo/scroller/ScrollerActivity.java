package com.example.zhihu.testviewdemo.scroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhihu.testviewdemo.R;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-25-2019
 */
public class ScrollerActivity extends AppCompatActivity {
    private ScrollerLinearView mView;
    private TextView mTextView;
    private int by = 0;
    int to = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        mView = findViewById(R.id.scroller_container);
        mTextView = findViewById(R.id.scroller_child);
    }

    public void scrollerClick(View view) {
        switch (view.getId()) {
            case R.id.to_btn:
                to += 10;
                mView.scrollTo(0, 100);
                break;
            case R.id.by_btn:
                mView.scrollBy(0, 10);
                break;
            case R.id.test_btn:
                Log.e("ScrollerActivity", "scrollerClick: ");
                mView.scrollerScroll(100);
                break;
        }
    }
}
