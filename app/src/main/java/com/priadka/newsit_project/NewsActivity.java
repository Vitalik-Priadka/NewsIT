package com.priadka.newsit_project;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsActivity extends Fragment {
    private static  final int LAYOUT = R.layout.news_layout;
    private View view;

    public static NewsActivity getInctance(){
        Bundle args = new Bundle();
        NewsActivity fragment = new NewsActivity();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }
}
