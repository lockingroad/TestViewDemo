package com.example.zhihu.testviewdemo.sticknav.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.example.zhihu.testviewdemo.R;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-22-2019
 */
public class MixtapeVideoContentView extends LinearLayout implements NestedScrollingParent {
    private MixtapeVideoView mVideoView;
    private View mTabLayout;
    private View mViewPager;
    private View mIntroView;
    private MixtapeVideoBottomContainer mContainer;
    private int mTopViewHeight;
    private int mIntroViewHeight;
    private int maxScrollY;
    private OverScroller mScroller;

    public boolean isEnableScroll() {
        return mEnableScroll;
    }

    private boolean mEnableScroll = true;
    private int mTotalHeight;

    String TAG = "StickNavLayout";

    public MixtapeVideoContentView(Context context) {
        super(context);
        init(context);
    }

    public MixtapeVideoContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MixtapeVideoContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        setOrientation(VERTICAL);
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mVideoView = findViewById(R.id.stick_video);
        mVideoView.setVideoListener(new MixtapeVideoView.VideoListener() {
            @Override
            public void onChange(boolean isPlaying) {
                setEnableScroll(!isPlaying);
            }
        });
        mTabLayout = findViewById(R.id.stick_tab);
        mViewPager = findViewById(R.id.stick_vp);
        mIntroView = findViewById(R.id.stick_intro);
        mContainer = findViewById(R.id.stick_container);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int pagerHeight = 0;
        if (mEnableScroll) {
            pagerHeight = parentHeight - mTabLayout.getMeasuredHeight();
        } else {
            pagerHeight = parentHeight - mTabLayout.getMeasuredHeight() - mVideoView.getMeasuredHeight();
        }
        mTotalHeight =
                mVideoView.getMeasuredHeight() + mTabLayout.getMeasuredHeight() + pagerHeight
                        + mIntroView.getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mTotalHeight);

        int linearHeight = parentHeight + mIntroViewHeight;
        mContainer.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(linearHeight, MeasureSpec.EXACTLY));
    }

    public void fling(int velocityY, int maxY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, maxY);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mVideoView.getMeasuredHeight();
        mIntroViewHeight = mIntroView.getMeasuredHeight();
        maxScrollY = mTopViewHeight + mIntroViewHeight;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight + mIntroViewHeight) {
            y = mTopViewHeight + mIntroViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedScroll:-> ");
        if (mEnableScroll) {
            boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight + mIntroViewHeight;
            // ViewCompat.canScrollVertically(target, -1) = false 时表示 view 已经到达最顶部，无法下拉了
            boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
//            Log.i(TAG, "onNestedPreScroll: dy " + dy);
//            Log.i(TAG, "onNestedPreScroll: scrollY" + getScrollY());
//            Log.i(TAG, "onNestedPreScroll: hiddenTop " + hiddenTop);
//            Log.i(TAG, "onNestedPreScroll: showTop " + showTop);
            if (hiddenTop || showTop) {
                scrollBy(0, dy);
                consumed[1] = dy;
            }
        } else {
            boolean hiddenMid = dy > 0 && mContainer.getScrollY() < mIntroViewHeight;
            boolean showMid = dy < 0 && mContainer.getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
            if (hiddenMid || showMid) {
                mContainer.scrollBy(0, dy);
                consumed[1] = dy;
            }
//            Log.e(TAG, "onNestedPreScroll: hiddenMid" + hiddenMid);
//            Log.e(TAG, "onNestedPreScroll: showMid" + showMid);
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "onNestedFling: " + velocityY + "consumed" + consumed);
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling:velocityY " + velocityY);
        Log.e(TAG, "onNestedPreFling:maxScrollY " + maxScrollY);
        Log.e(TAG, "onNestedPreFling:getScrollY " + getScrollY());
        if (mEnableScroll) {
            if (getScrollY() < maxScrollY) {
                fling((int) velocityY, maxScrollY);
                return true;
            }
        } else {
            if (mContainer.isFold()) {
                mContainer.fling((int) velocityY);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll: dyConsumed" + dyConsumed);
        Log.e(TAG, "onNestedScroll: dyUnconsumed" + dyUnconsumed);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
    }


    @Override
    public void onStopNestedScroll(View child) {
    }

    @Override
    public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    public void setEnableScroll(boolean enableScroll) {
        mEnableScroll = enableScroll;
    }
}
