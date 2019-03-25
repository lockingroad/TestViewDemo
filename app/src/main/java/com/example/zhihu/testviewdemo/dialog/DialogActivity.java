package com.example.zhihu.testviewdemo.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.zhihu.testviewdemo.R;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-24-2019
 */
public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    public void dialogClick(View view) {
        switch (view.getId()) {
            case R.id.frag_btn:
                MainDialogFragment dialogFragment = new MainDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                break;
        }
    }
}
