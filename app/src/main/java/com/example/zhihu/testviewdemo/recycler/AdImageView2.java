package com.example.zhihu.testviewdemo.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * @author liuran @ Zhihu Inc.
 * @since 03-18-2019
 */
public class AdImageView2 extends CardView {
    private Context mContext;
    private AppCompatImageView mCoverView;
    String TAG = "AdImageView2";
    public AdImageView2(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AdImageView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdImageView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mCoverView = new AppCompatImageView(mContext);
        mCoverView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.setCardElevation(50f);
        this.setClipChildren(true);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        addView(mCoverView,params);
    }

    public void setBitmap(Bitmap pBitmap) {

        int screenWidth = AdUtils.getScreenSizeX(getContext());
        int screenHeight = AdUtils.getScreenSizeY(getContext());
        int sourceWidth = pBitmap.getWidth();
        int sourceHeight = pBitmap.getHeight();

        int targetHeight = sourceWidth * screenHeight / screenWidth;
        if (targetHeight > sourceHeight) {
            targetHeight = sourceHeight;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(1, 1);
        Log.e(TAG, "setBitmap: sourceWidth" + sourceWidth);
        Log.e(TAG, "setBitmap: targetHeight" + targetHeight);
        Bitmap targetBitmap = Bitmap.createBitmap(
                pBitmap,
                0,
                0,
                sourceWidth,
                1550,
                matrix,
                true);
        this.mCoverView.setImageBitmap(targetBitmap);
    }


    public void dragPosition(int left, int top, int right, int bottom) {
        layout(left, top, right, bottom);
    }

    public void reset() {
        this.layout(0, 0, this.getWidth(), this.getHeight());
    }

}
