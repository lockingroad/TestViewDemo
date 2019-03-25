package com.example.zhihu.testviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-15-2019
 */
public class ClickSpannableActivity extends AppCompatActivity {


    private CommonUrlTextView mDesc1;
    private CommonUrlTextView mDesc2;
    private CommonUrlTextView mDesc3;
    String content1 = "买盐选会员年卡，赠京东年卡（含爱奇艺卡） <a href=\"http 查看权益\">查看权益</a>";
    String content2 = "设置出现在推荐页设置出现在推荐页设置出现在推荐页设置出现在推荐页。 <a href=\"http 去设置\">去设置</a>";
    String content3 = "60 天可修改一次用户名。 <a href=\"http 去修改\">去修改</a>";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_spannable);
        mDesc1 = findViewById(R.id.desc1);
        mDesc2 = findViewById(R.id.desc2);
        mDesc3 = findViewById(R.id.desc3);

        mDesc1.setContent(content1);
        mDesc2.setContent(content2);
        mDesc3.setContent(content3);

    }
}
