package com.example.zhihu.testviewdemo.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-17-2019
 */
public class AdImageView extends AppCompatImageView {
    private float traPercent;
    private int minHeight;
    private String TAG = "AdImageView";


    public AdImageView(Context context) {
        super(context);
    }

    public AdImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        minHeight = h;
        Log.e(TAG, "onSizeChanged: " + minHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        int w = getWidth();
        int h = (int) (getWidth() * 1.0f / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, w, h);
        canvas.save();
        canvas.translate(0, -(h - minHeight) * traPercent);
        super.onDraw(canvas);
//        canvas.restore();
    }

    public void setTraPercent(int childHeight, int parentHeight, int orientation) {
        if (getDrawable() == null) {
            return;
        }
        float traPercent;
        if (orientation == 0) {
            traPercent = childHeight * 1f / (parentHeight - minHeight);
        } else {
            traPercent = (parentHeight - childHeight - minHeight) * 1f / (parentHeight - minHeight);
        }
        this.traPercent = traPercent;
        if (this.traPercent <= 0) {
            this.traPercent = 0;
        }
        if (this.traPercent >= 1) {
            this.traPercent = 1;
        }
        invalidate();
    }

    public void testTraPercent(float f) {
        traPercent = f;
        invalidate();

    }


}
