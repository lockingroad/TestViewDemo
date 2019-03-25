package com.example.zhihu.testviewdemo.sticknav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.example.zhihu.testviewdemo.R;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-25-2019
 */
public class MixtapeContentBottomContainer extends LinearLayout {
    private View mIntroView;
    private int mIntroViewHeight;
    private OverScroller mScroller;
    public MixtapeContentBottomContainer(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mScroller = new  OverScroller(context);
    }

    public MixtapeContentBottomContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MixtapeContentBottomContainer(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIntroView = findViewById(R.id.stick_intro);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mIntroViewHeight = mIntroView.getMeasuredHeight();
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        if (y < 0) {
            y = 0;
        }
        if (y > mIntroViewHeight) {
            y = mIntroViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    public void fling(int velocityY)
    {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mIntroViewHeight);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public boolean isFold() {
        return getScrollY() < mIntroViewHeight;
    }
}
