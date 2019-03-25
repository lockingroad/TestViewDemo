package com.example.zhihu.testviewdemo.sticknav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class PersonalScrollView extends ScrollView {

    public PersonalScrollView(Context context) {
        super(context);
    }

    public PersonalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PersonalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        View view = (View) getChildAt(getChildCount() - 1);

        int d = view.getBottom();

        d -= (getHeight() + getScrollY());

        if ((d == 0) && (onScrollBottomListener != null)) {
            onScrollBottomListener.onScrollBottom();
        }
    }

    public OnScrollBottomListener onScrollBottomListener = null;

    public interface OnScrollBottomListener {
        void onScrollBottom();
    }

    public void setOnScrollBottomListener(OnScrollBottomListener onScrollBottomListener) {
        this.onScrollBottomListener = onScrollBottomListener;
    }
}
