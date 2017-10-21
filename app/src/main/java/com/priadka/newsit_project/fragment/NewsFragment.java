package com.priadka.newsit_project.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.priadka.newsit_project.Adapter.NewsListAdapter;
import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.DTO.NewsDTO;
import com.priadka.newsit_project.R;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private View view;
    protected Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        view = inflater.inflate(Constant.NEWS, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycleViewNews);
        rv.setLayoutManager( new LinearLayoutManager(context));
        rv.setAdapter(new NewsListAdapter(createMockListNews()));
        return view;
    }

    // Временная заглушка
    private List<NewsDTO> createMockListNews() {
        List<NewsDTO> data = new ArrayList<>();
        data.add(new NewsDTO("Title#1 Статья о Книгах","13:57 22.10.17", 50, 26));
        data.add(new NewsDTO("Title#2 Статья о Выпичке","12:12 14.07.17", 124, 45));
        data.add(new NewsDTO("Title#3 Ах да, это же IT блог","16:28 26.08.17", 24, 4));
        data.add(new NewsDTO("Title#4 Статья о Роботах","17:34 06.08.17", 43, 3));
        data.add(new NewsDTO("Title#5 Статья о Инскуственном интелекте в банане","19:13 23.09.17", 76, 63));
        data.add(new NewsDTO("Title#6 Лень писать...","12:13 02.09.17", 86, 65));
        data.add(new NewsDTO("Title#7 Лень писать...","18:27 23.09.17", 36, 13));
        data.add(new NewsDTO("Title#8 Лень писать...","13:53 23.09.17", 96, 23));
        data.add(new NewsDTO("Title#9 Лень писать...","12:49 23.09.17", 36, 34));
        data.add(new NewsDTO("Title#10 Пингвины летают! ШОК! Ученые выяснили что это агенты КГБ!","19:13 23.09.17", 999, 228));
        return data;
    }
}
