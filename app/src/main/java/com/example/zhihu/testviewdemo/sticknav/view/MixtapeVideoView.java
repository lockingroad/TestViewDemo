package com.example.zhihu.testviewdemo.sticknav.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-21-2019
 */
public class MixtapeVideoView  extends FrameLayout {
    private Button mPlayBtn;
    private boolean mPlaying;
    private VideoListener mVideoListener;

    public void setVideoListener(VideoListener videoListener) {
        mVideoListener = videoListener;
    }

    public MixtapeVideoView(Context context) {
        super(context);
        init(context);
    }


    public MixtapeVideoView( Context context,
              AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MixtapeVideoView( Context context,
              AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mPlayBtn = new Button(context);
        mPlayBtn.setText("点击播放");
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(mPlayBtn, params);
        mPlayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaying = !mPlaying;
                if (mVideoListener != null) {
                    mVideoListener.onChange(mPlaying);
                }
                if (mPlaying) {
                    mPlayBtn.setText("播放中");
                } else {
                    mPlayBtn.setText("暂停中");
                }
            }
        });
        setBackgroundColor(Color.BLACK);
    }

    interface VideoListener {
        void onChange(boolean isPlaying);
    }
}
