package com.example.zhihu.testviewdemo;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * @author liuran @ Zhihu Inc.
 * @since 01-25-2019
 */
public class ScrollFrescoActivity extends AppCompatActivity {
    private SimpleDraweeView mSimpleDraweeView;
    private SimpleDraweeView mSimpleDraweeView1;
    private SimpleDraweeView mSimpleDraweeView2;
    private Button frescoButton;
    private LinearLayoutCompat container;
    private String TAG = "ScrollFrescoActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_fresco);
        mSimpleDraweeView = findViewById(R.id.scroll_img);
        mSimpleDraweeView1 = findViewById(R.id.scroll_img1);
        mSimpleDraweeView2 = findViewById(R.id.scroll_img2);
        frescoButton = findViewById(R.id.fresco_bottom);
        container = findViewById(R.id.scroll_linear);
//        mSimpleDraweeView.setImageURI("https://pic2.zhimg.com/v2-5dab3245dd346d8e7eab17ed9ba18ab6_r.jpg");
        frescoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleDraweeView.setImageURI("https://pic2.zhimg.com/v2-5dab3245dd346d8e7eab17ed9ba18ab6_r.jpg");
                mSimpleDraweeView.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.EXACTLY);
                mSimpleDraweeView1.setImageURI("https://pic2.zhimg.com/v2-5dab3245dd346d8e7eab17ed9ba18ab6_r.jpg");
                mSimpleDraweeView2.setImageURI("https://pic2.zhimg.com/v2-5dab3245dd346d8e7eab17ed9ba18ab6_r.jpg");
                Log.e(TAG, "onClick: " + mSimpleDraweeView2.getMeasuredHeight());

            }
        });


        DraweeController controller = Fresco.newDraweeControllerBuilder().setUri("https://pic2.zhimg.com/v2-5dab3245dd346d8e7eab17ed9ba18ab6_r.jpg")
                .setControllerListener(listener)
                .build();
        mSimpleDraweeView2.setController(controller);



    }

    void updateViewSize(@Nullable ImageInfo imageInfo) {
        if (imageInfo != null) {
            mSimpleDraweeView2.getLayoutParams().width = imageInfo.getWidth();
            mSimpleDraweeView2.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mSimpleDraweeView2.setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
        }
    }

    ControllerListener listener = new BaseControllerListener() {
        @Override
        public void onIntermediateImageSet(String id, Object imageInfo) {
            Log.e(TAG, "onIntermediateImageSet: " );
            if (imageInfo instanceof ImageInfo) {
                Log.e(TAG, "onIntermediateImageSet: instanceof" );
                updateViewSize((ImageInfo) imageInfo);
            }
        }

        @Override
        public void onFinalImageSet(String id, Object imageInfo,
                Animatable animatable) {
            Log.e(TAG, "onFinalImageSet: " );
            if (imageInfo instanceof ImageInfo) {
                Log.e(TAG, "onFinalImageSet: instaceof ");
                updateViewSize((ImageInfo) imageInfo);
            }
        }
    };
}
