package com.priadka.newsit_project.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.priadka.newsit_project.Adapter.NewsListAdapter;
import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;

public class NewsFragment extends Fragment {
    private View view;
    protected Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        view = inflater.inflate(Constant.NEWS, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycleViewNews);
        rv.setLayoutManager( new LinearLayoutManager(context));
        MainActivity mainActivity = new MainActivity();
        rv.setAdapter(new NewsListAdapter(mainActivity.getNews()));
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        NestedScrollView scrollViewNews = (NestedScrollView) getActivity().findViewById(R.id.scrollViewNews);
        int y = scrollViewNews.getScrollY();
        scrollViewNews.smoothScrollTo(0, y);
    }
}
