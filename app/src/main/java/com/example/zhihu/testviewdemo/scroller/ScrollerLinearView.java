package com.example.zhihu.testviewdemo.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.Scroller;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-25-2019
 */
public class ScrollerLinearView extends LinearLayout {
    private OverScroller mScroller;
    public ScrollerLinearView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mScroller = new OverScroller(context);
    }

    public ScrollerLinearView(Context context,   AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollerLinearView(Context context,   AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void scrollerScroll(int dy) {
        mScroller.startScroll(0,0,0,200);
        invalidate();

    }

    @Override
    public void computeScroll() {
        Log.e("ScrollerLinearView", "computeScroll: " );
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            Log.e("ScrollerLinearView", "computeScroll: " + mScroller.getCurrY());
            postInvalidate();
        }
    }
}
