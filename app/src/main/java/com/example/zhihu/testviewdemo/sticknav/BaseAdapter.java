package com.example.zhihu.testviewdemo.sticknav;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhihu.testviewdemo.R;

import java.util.List;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-21-2019
 */
public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.BaseHolder>{

    public List<String> mList;

    public BaseAdapter(List<String> list) {
        mList = list;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_ad_normal, null);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        baseHolder.mTv.setText(mList.get(i));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class BaseHolder extends RecyclerView.ViewHolder {
        public TextView mTv;
        public BaseHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.item_tv);
        }
    }
}
