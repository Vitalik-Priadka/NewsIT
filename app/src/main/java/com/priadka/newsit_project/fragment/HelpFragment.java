package com.priadka.newsit_project.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;
// Класс для отображения информации пользователю
// состояния сети или "закладок"
public class HelpFragment extends Fragment {
    private TextView stateServer;
    private TextView stateBookmark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstaneState) {
        return inflater.inflate(Constant.HELP, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // При создании фрагмента инициализация переменных
        stateServer = (TextView) getView().findViewById(R.id.serverError);
        stateBookmark = (TextView) getView().findViewById(R.id.bookmarksError);
        showState();
    }
    // Показ сообщения stateBookmark/stateServer
    private void showState() {
        Boolean isConnect = ((MainActivity)getActivity()).getIsConnect();
        if (isConnect && ((MainActivity)getActivity()).getUser() != null){
            stateServer.setVisibility(View.GONE);
            if (((MainActivity)getActivity()).getUser().getUser_bookmarksList().isEmpty()){
                stateBookmark.setVisibility(View.VISIBLE);
            }
        }
        else{
            stateServer.setVisibility(View.VISIBLE);
            stateBookmark.setVisibility(View.GONE);
        }
    }
}
