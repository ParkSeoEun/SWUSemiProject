package com.example.swusemiproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.swusemiproject.Database.DB;
import com.example.swusemiproject.model.Memo;

import java.util.ArrayList;
import java.util.List;

public class MemoList extends Fragment {

    public static final int SAVE =1001;

    // 원본 데이터
    List<Memo> memos = new ArrayList<>();
    ListAdapter adapter;

    Button btnNewMemo;

    ListView listView;

    public MemoList() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Fragment UI 생성
        View view = inflater.inflate(R.layout.fragment_memo_list,container,false);

        btnNewMemo = view.findViewById(R.id.btnNewMemo);
        listView = view.findViewById(R.id.listView);

        //memos = DB.getInstance().loadMemos();


        btnNewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MemoWriteActivity.class));
            }
        });

        return view;
    }
}
