package com.example.zhihu.testviewdemo.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhihu.testviewdemo.R;
import com.example.zhihu.testviewdemo.sticknav.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuran @ Zhihu Inc.
 * @since 03-23-2019
 */
public class RecyclerActivity2 extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler2);
        mRecyclerView = findViewById(R.id.recycler_view);


        List<String> list = new ArrayList<>();
        for (int i = 0; i< 30; i++) {
            list.add("item" + String.valueOf(i));
        }
        mAdapter = new BaseAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }
}
