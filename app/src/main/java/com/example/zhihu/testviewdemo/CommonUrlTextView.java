package com.example.zhihu.testviewdemo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CommonUrlTextView extends AppCompatTextView {
    public ColorStateList linkColor;
    private Context mContext;

    public CommonUrlTextView(Context context) {
        this(context,null);
        mContext = context;
    }

    public CommonUrlTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        mContext = context;
    }

    public CommonUrlTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.PremiumUrlAttrs);
        linkColor = typedArray.getColorStateList(R.styleable.PremiumUrlAttrs_premium_link_color);
    }


    public void setContent(String text) {


        CharSequence htmlText = Html.fromHtml(parseContent(text));
        super.setText(htmlText);
        if (htmlText instanceof Spannable) {
            Spannable sp = (Spannable) htmlText;
            setMovementMethod(LinkMovementMethod.getInstance());
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(htmlText);

            final URLSpan[] urlSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(),
                    URLSpan.class);

            SpannableStringBuilder style = new SpannableStringBuilder(htmlText);
            style.clearSpans();//should clear old spans
            for (final URLSpan urlSpan : urlSpans) {

                MyURLSpan myURLSpan = new MyURLSpan(urlSpan.getURL(), getContext(), linkColor);
                Log.e("VIPAdapter",  "span " + urlSpan.getURL());
                int start = sp.getSpanStart(urlSpan);
                Log.e("VIPAdapter",  "span start " + start);
                int end = sp.getSpanEnd(urlSpan);
                Log.e("VIPAdapter",  "span end " + end);

                style.setSpan(myURLSpan, start, end,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            setText(style);
        }
    }

    String TAG = "VIPAdapter";
    public void setLinkColor(ColorStateList linkColor) {
        this.linkColor = linkColor;
    }

    /**
     * 把 /n 转换为 <br>，解决 Html.fromHtml(source) 遇到“\n”不能换行的问题
     */
    private String parseContent(String content) {
        if (content != null) {
            content = content.replace("\n", "<br>");
        }
        return content;
    }

    private static class MyURLSpan extends ClickableSpan {

        private ColorStateList mLinkColor;
        private Context mContext;
        private String mUrl;

        MyURLSpan(String url) {
            mUrl = url;
        }

        MyURLSpan(String url, Context context, ColorStateList linkColor) {
            mUrl = url;
            mContext = context;
            mLinkColor = linkColor;
        }

        @Override
        public void onClick(View widget) {
            Log.e("VIPAdapter", "onclick" + mUrl);
        }


        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            if (mContext != null) {
                ds.setColor(mLinkColor.getDefaultColor());//设置超链接颜色
            }
        }
    }
}
