package com.example.zhihu.testviewdemo.recycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.example.zhihu.testviewdemo.DisplayUtils;
import com.example.zhihu.testviewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-15-2019
 */
public class RecyclerADActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private int type = 0;
    private int orientation;
    private LinearLayoutManager linearLayoutManager;
    private AdImageView2 mAdImageView2;
    int topPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_ad);
        orientation = getIntent().getIntExtra("index", 0);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = ((RecyclerView) findViewById(R.id.recycler));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new TestAdapter(new AlpahCallBack() {
            @Override
            public void call() {
                type = 1;
                recyclerView.animate().alpha(0).setDuration(400).setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        recyclerView.setVisibility(View.GONE);
                    }
                }).start();
            }
        }));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int endPosition = linearLayoutManager.findLastVisibleItemPosition();
                for (int i = firstPosition; i <= endPosition; i++) {
                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
                    if (holder instanceof AlphaHolder) {
                        View view = holder.itemView;
                        AdImageView canTraImageView = ((AlphaHolder) holder).canTraImageView;
                        canTraImageView.setTraPercent(view.getTop(), linearLayoutManager.getHeight(), orientation);
                    }
                }
            }
        });


        LinearLayout linearLayout = findViewById(R.id.ad_linear);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linearLayout, "scaleY", 1f, 2f);
        objectAnimator.setDuration(2000);
        objectAnimator.start();


        FrameLayout frameLayout = findViewById(R.id.ad_container);

        mAdImageView2 = new AdImageView2(this);
        FrameLayout.LayoutParams backParam = new FrameLayout.LayoutParams(
                AdUtils.getScreenSizeX(this),
                AdUtils.getScreenSizeY(this)
        );
        backParam.gravity = Gravity.TOP;
        backParam.topMargin = 0;
        backParam.leftMargin = 0;
        frameLayout.addView(mAdImageView2, backParam);
        Bitmap china = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        mAdImageView2.setBitmap(china);



//        testBitmap();

        findViewById(R.id.adBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topPosition -= 10;
                mAdImageView2.dragPosition(mAdImageView2.getLeft(), topPosition, mAdImageView2.getRight(), topPosition+mAdImageView2.getHeight());
            }
        });

        findViewById(R.id.adResetBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet set = new AnimatorSet();
                frameLayout.setPivotY(DisplayUtils.dpToPixel(RecyclerADActivity.this, 140));
                frameLayout.setPivotX(frameLayout.getWidth()/2);
                List<Animator>list = new ArrayList<>();
                ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(frameLayout, View.SCALE_Y, 1, 2f);
                ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(frameLayout, View.SCALE_X, 1, 2f);
                list.add(scaleAnimatorY);
                list.add(scaleAnimatorX);
                set.playTogether(list);
                set.setDuration(2000);
                set.start();

            }
        });
    }

    private void testBitmap() {
        Bitmap pBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        int screenWidth = AdUtils.getScreenSizeX(this);
        int screenHeight = AdUtils.getScreenSizeY(this);
        int sourceWidth = pBitmap.getWidth();
        int sourceHeight = pBitmap.getHeight();

        int targetHeight = sourceWidth * screenHeight / screenWidth;
        if (targetHeight > sourceHeight) {
            targetHeight = sourceHeight;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(1, 1);
        Bitmap targetBitmap = Bitmap.createBitmap(
                pBitmap,
                0,
                0,
                sourceWidth,
                targetHeight,
                matrix,
                true);
        AppCompatImageView imageView = findViewById(R.id.ad_img);
        imageView.setImageBitmap(targetBitmap);
    }

    private class TestAdapter extends RecyclerView.Adapter {

        private AlpahCallBack callBack;

        public TestAdapter(AlpahCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return viewType == 1 ? new AlphaHolder(
                    LayoutInflater.from(RecyclerADActivity.this).inflate(R.layout.item_ad_content, parent, false)) :
                    new NormalHolder(LayoutInflater.from(RecyclerADActivity.this).inflate(R.layout.item_ad_normal, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof AlphaHolder) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callBack != null) {
                            callBack.call();
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return 40;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 8 ? 1 : 0;
        }
    }

    private class NormalHolder extends RecyclerView.ViewHolder {

        public NormalHolder(View itemView) {
            super(itemView);
        }
    }

    private class AlphaHolder extends RecyclerView.ViewHolder {

        AdImageView canTraImageView;

        public AlphaHolder(View itemView) {
            super(itemView);
            canTraImageView = (AdImageView) itemView.findViewById(R.id.item);
        }
    }

    private interface AlpahCallBack {
        void call();
    }

    @Override
    public void onBackPressed() {
        if (type == 1) {
            type = 0;
            recyclerView.animate().alpha(1).setDuration(400).setListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }).start();
        } else {
            super.onBackPressed();
        }
    }


}
