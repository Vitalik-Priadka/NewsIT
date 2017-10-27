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

    @Override
    public void onPause() {
        super.onPause();
        NestedScrollView scrollViewNews = (NestedScrollView) getActivity().findViewById(R.id.scrollViewNews);
        int y = scrollViewNews.getScrollY();
        scrollViewNews.smoothScrollTo(0, y);
    }

    // Временная заглушка
    private List<NewsDTO> createMockListNews() {
        List<NewsDTO> data = new ArrayList<>();
        data.add(new NewsDTO(101 ,1, "Title#1 Статья о Роботах","Текс статьи №1","13:57 22.10.17", 50, 26));
        data.add(new NewsDTO(102 ,2, "Title#2 Статья о Взрывах","Текс статьи №2","12:12 14.07.17", 124, 45));
        data.add(new NewsDTO(103 ,3, "Title#3 Статья о Космосе","Текс статьи №3","16:28 26.08.17", 24, 4));
        data.add(new NewsDTO(104 ,4, "Title#4 Статья о Рыбалке Эм..","Текс статьи №4","17:34 06.08.17", 43, 3));
        data.add(new NewsDTO(105 ,5, "Title#5 Статья о Кибернетике","Текс статьи №5","19:13 23.09.17", 76, 63));
        data.add(new NewsDTO(106 ,6, "Title#6 Статья о Пришельцах","Текс статьи №6","12:13 02.09.17", 86, 65));
        data.add(new NewsDTO(107 ,7, "Title#7 Статья о Xiaomi","Текс статьи №7","18:27 23.09.17", 36, 13));
        data.add(new NewsDTO(108 ,8, "Title#8 Статья о Apple","Текс статьи №8","13:53 23.09.17", 96, 23));
        data.add(new NewsDTO(109 ,9, "Title#9 Статья о Samsung","Текс статьи №9","12:49 23.09.17", 36, 34));
        data.add(new NewsDTO(110 ,10, "Title#10 Статья о Машинах","Текс статьи №10","19:13 23.09.17", 999, 228));
        return data;
    }
}
