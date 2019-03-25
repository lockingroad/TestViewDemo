package com.example.zhihu.testviewdemo.sticknav;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.example.zhihu.testviewdemo.R;
import com.example.zhihu.testviewdemo.sticknav.view.MixtapeContentBottomContainer;
import com.example.zhihu.testviewdemo.sticknav.view.MixtapeContentIntroView;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-22-2019
 */
public class StickNavLayout extends LinearLayout implements NestedScrollingParent {

    private int TOP_CHILD_FLING_THRESHOLD = 3;

    private MixtapeVideoView mTop;
    private View mNav;
    private ViewPager mViewPager;
    private View mMid;
    private int mTopViewHeight;
    private int mMidViewHeight;
    private int maxScrollY;
    private OverScroller mScroller;
    private ValueAnimator mOffsetAnimator;
    private int mOffset;
    private int totalY = 0;
    private MixtapeContentBottomContainer mContainer;

    public boolean isEnableScroll() {
        return mEnableScroll;
    }

    private boolean mEnableScroll = true;
    private int mTotalHeight;

    String TAG = "StickNavLayout";

    public StickNavLayout(Context context) {
        super(context);
        init(context);
    }

    public StickNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickNavLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        setOrientation(VERTICAL);
        mScroller = new OverScroller(context);
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (mEnableScroll) {
            boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight + mMidViewHeight - mOffset;
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
            boolean hiddenMid = dy > 0 && mContainer.getScrollY() < mMidViewHeight;
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
        //如果是recyclerView 根据判断第一个元素是哪个位置可以判断是否消耗
        //这里判断如果第一个元素的位置是大于TOP_CHILD_FLING_THRESHOLD的
        //认为已经被消耗，在animateScroll里不会对velocityY<0时做处理
        Log.e(TAG, "onNestedFling: " + velocityY + "consumed" + consumed);
//        if (velocityY < 0) {
//            if (target instanceof RecyclerView) {
//                final RecyclerView recyclerView = (RecyclerView) target;
//                final View firstChild = recyclerView.getChildAt(0);
//                final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
//                consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD;
//            } else if (target instanceof WebView) {
//                consumed = !(target.getScrollY() == 0);
//            }
//        }
//        if (!consumed) {
//            animateScroll(velocityY, computeDuration(0), consumed);
//        } else {
//            animateScroll(velocityY, computeDuration(velocityY), consumed);
//        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling: " + velocityY);
        if (mEnableScroll) {
            if (getScrollY()<maxScrollY) {
                fling((int) velocityY, maxScrollY);
                return true;
            }
        }else {
            if (mContainer.isFold()) {
                mContainer.fling((int) velocityY);
                return true;
            }
        }
        return false;
    }


    private void animateScroll(float velocityY, final int duration, boolean consumed) {
        final int currentOffset = getScrollY();
        final int topHeight = mTop.getHeight();
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedValue() instanceof Integer) {
                        scrollTo(0, (Integer) animation.getAnimatedValue());
                    }
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }
        mOffsetAnimator.setDuration(Math.min(duration, 600));

        if (velocityY >= 0) {
            mOffsetAnimator.setIntValues(currentOffset, topHeight - mOffset);
            mOffsetAnimator.start();
        } else {
            //如果子View没有消耗down事件 那么就让自身滑倒0位置
            if (!consumed) {
                mOffsetAnimator.setIntValues(currentOffset, 0);
                mOffsetAnimator.start();
            }
        }
    }

    private int computeDuration(float velocityY) {
        final int distance;
        if (velocityY > 0) {
            distance = Math.abs(mTop.getHeight() - getScrollY());
        } else {
            distance = Math.abs(mTop.getHeight() - (mTop.getHeight() - getScrollY()));
        }
        final int duration;
        velocityY = Math.abs(velocityY);
        if (velocityY > 0) {
            duration = 3 * Math.round(1000 * (distance / velocityY));
        } else {
            final float distanceRatio = (float) distance / getHeight();
            duration = (int) ((distanceRatio + 1) * 150);
        }

        return duration;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.stick_video);
        mTop.setVideoListener(new MixtapeVideoView.VideoListener() {
            @Override
            public void onChange(boolean isPlaying) {
                setEnableScroll(!isPlaying);
            }
        });
        mNav = findViewById(R.id.stick_tab);
        mViewPager = findViewById(R.id.stick_vp);
        mMid = findViewById(R.id.stick_intro);
        mContainer = findViewById(R.id.stick_container);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int pagerHeight = 0;
        if (mEnableScroll) {
            pagerHeight = parentHeight - mNav.getMeasuredHeight();
        } else {
            pagerHeight = parentHeight - mNav.getMeasuredHeight() - mTop.getMeasuredHeight();
        }
        mTotalHeight = mTop.getMeasuredHeight() + mNav.getMeasuredHeight() + pagerHeight + mMid.getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mTotalHeight);

        int linearHeight = parentHeight + mMidViewHeight;
        mContainer.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(linearHeight, MeasureSpec.EXACTLY));
    }

    public void fling(int velocityY, int maxY)
    {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, maxY);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
        mMidViewHeight = mMid.getMeasuredHeight();
        maxScrollY = mTopViewHeight + mMidViewHeight;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight + mMidViewHeight - mOffset) {
            y = mTopViewHeight + mMidViewHeight - mOffset;
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
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
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

    public interface OnScrollListener {
        void onScroll(int scrolled, int total);
    }
}
