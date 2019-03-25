package com.example.zhihu.testviewdemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zhihu.testviewdemo.dialog.DialogActivity;
import com.example.zhihu.testviewdemo.draghelper.DragActivity;
import com.example.zhihu.testviewdemo.ebookdownload.EBookDownloadSimpleButton;
import com.example.zhihu.testviewdemo.recycler.RecyclerADActivity;
import com.example.zhihu.testviewdemo.recycler.RecyclerActivity2;
import com.example.zhihu.testviewdemo.scroller.ScrollerActivity;
import com.example.zhihu.testviewdemo.sticknav.StickNavActivity;
import com.example.zhihu.testviewdemo.sticknav.StickNavActivity2;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private EBookDownloadSimpleButton mDownloadBtn;
    private String TAG = "MainActivity";
    private int count;
    private TextView mTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDownloadBtn = findViewById(R.id.download_btn);
        mTv = findViewById(R.id.main_tv);

        mDownloadBtn.setDownloadStatusListener(new EBookDownloadSimpleButton.DownloadStatusInterface() {
            @Override
            public void startDownload() {
                Log.e(TAG, "startDownload: ");
                count = 0;
                mDownloadBtn.setStatus(EBookDownloadSimpleButton.STATUS_DOWNLOADING);
                Observable.interval(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(nl -> {
                            count += 5;
                            if (count<100) {
                                mDownloadBtn.setDownloadProgress(count, true);
                            } else {
                                mDownloadBtn.setStatus(EBookDownloadSimpleButton.STATUS_FINISHED);
                            }

                        });

            }



            @Override
            public void stopDownload() {

                Log.e(TAG, "stopDownload: ");
            }

            @Override
            public void continueDownload() {

                Log.e(TAG, "continueDownload: " );
            }
        });


        mDownloadBtn.setOnBtnClickListener(new EBookDownloadSimpleButton.OnBtnClickListener() {
            @Override
            public void onFinishStatusButtonClicked() {

                Log.e(TAG, "onFinishStatusButtonClicked: ");
            }
        });

        Single.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "nl" + aLong);

                        Drawable drawable = getDrawable(R.drawable.ic_km_sku_bottom_enter_book);
                        drawable.setTint(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                        mTv.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
                    }
                });

        StatusBarUtil.hideStatusBar(this);
    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.toScroll:
                StatusBarUtil.showStatusBar(this);
                Intent intent = new Intent(MainActivity.this, ScrollFrescoActivity.class);
                startActivity(intent);
                break;
            case R.id.hide:
                StatusBarUtil.hideStatusBar(this);
                break;
            case R.id.show:
                StatusBarUtil.showStatusBar(this);
                break;

            case R.id.spannable_click:
                Intent intent1 = new Intent(MainActivity.this, ClickSpannableActivity.class);
                startActivity(intent1);
                break;
            case R.id.recycler_ad:
                Intent intentAd = new Intent(MainActivity.this, RecyclerADActivity.class);
                startActivity(intentAd);
                break;

            case R.id.stick_nav:
                startActivity(StickNavActivity.class);
                break;
            case R.id.stick_nav1:
                startActivity(StickNavActivity2.class);
                break;
            case R.id.main_drag:
                startActivity(DragActivity.class);
                break;

            case R.id.recycler2:
                startActivity(RecyclerActivity2.class);
                break;

            case R.id.dialog:
                startActivity(DialogActivity.class);
                break;

            case R.id.scroller:
                startActivity(ScrollerActivity.class);
                break;

        }

    }

    private void startActivity(Class cls) {
        startActivity(new Intent(MainActivity.this, cls));
    }
}
