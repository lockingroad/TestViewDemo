package com.example.zhihu.testviewdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhihu.testviewdemo.sticknav.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-21-2019
 */
public class BaseRecyclerFragment extends Fragment {

    String title = "arg";

    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;

    public static BaseRecyclerFragment getInstance(String title) {
        BaseRecyclerFragment fragment = new BaseRecyclerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("arg", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("arg", "title");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.base_recycler);
        List<String>list = new ArrayList<>();
        for (int i = 0; i< 30; i++) {
            list.add(title + String.valueOf(i));
        }
        mAdapter = new BaseAdapter(list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }
}
