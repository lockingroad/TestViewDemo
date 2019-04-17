package com.example.zhihu.testviewdemo.animation;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.zhihu.testviewdemo.R;

/**
 * @author liuran @ Zhihu Inc.
 * @since 04-15-2019
 */
public class AnimationActivity extends AppCompatActivity {
    private FrameLayout fl;
    private ConstraintLayout container;
    private ObjectAnimator mAnimator;
    private View view1;
    String TAG = "AnimationActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animaiton);
        fl = findViewById(R.id.fl);
        container = findViewById(R.id.container);
        View view = View.inflate(this, R.layout.view_animation, null);
        fl.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        view1 = view.findViewById(R.id.view1);
        view.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);
        Log.e(TAG, "onCreate: " + view1.getMeasuredWidth());

        mAnimator = ObjectAnimator.ofFloat(view1, "translationX", 0, view1.getMeasuredWidth());
        mAnimator.setDuration(1000);
    }

    public void animClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Log.e(TAG, "animClick: "+ view1.getMeasuredWidth() );
                mAnimator.start();
                break;
        }
    }
}
