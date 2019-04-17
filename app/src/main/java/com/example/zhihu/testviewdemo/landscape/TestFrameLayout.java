package com.example.zhihu.testviewdemo.landscape;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author liuran @ Zhihu Inc.
 * @since 04-07-2019
 */
public class TestFrameLayout extends FrameLayout {

    String TAG = "TestFrameLayout";
    public TestFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public TestFrameLayout(Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestFrameLayout(Context context,
            AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Log.e(TAG, "init: ");
        PlayControlView view = new PlayControlView(context);
        FrameLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view,lp);
    }

}
