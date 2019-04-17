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
public class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<String> mList;

    public BaseAdapter(List<String> list) {
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 1) {
            View adView = View.inflate(viewGroup.getContext(), R.layout.item_ad_content, null);
            return new ADHolder(adView);
        }
        View view = View.inflate(viewGroup.getContext(), R.layout.item_ad_normal, null);
        return new BaseHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 10) return 1;
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof BaseHolder) {
            ((BaseHolder) holder).mTv.setText(mList.get(i));
        }
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

    class ADHolder extends RecyclerView.ViewHolder {
        public ADHolder(View itemView) {
            super(itemView);
        }
    }
}
