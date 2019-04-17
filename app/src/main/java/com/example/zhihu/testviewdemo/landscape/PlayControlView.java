package com.example.zhihu.testviewdemo.landscape;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.example.zhihu.testviewdemo.R;

/**
 * @author liuran @ Zhihu Inc.
 * @since 04-04-2019
 */
public class PlayControlView extends FrameLayout {
    public PlayControlView(Context context) {
        super(context);
        init(context);
    }


    public PlayControlView(Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayControlView(Context context,
            AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_play_control, this, true);
    }
}
