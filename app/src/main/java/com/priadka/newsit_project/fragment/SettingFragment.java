package com.priadka.newsit_project.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;

public class SettingFragment extends Fragment {
    private Spinner spinnerLanguage;
    private Spinner spinnerTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(Constant.SETTING, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // При создании фрагмента инициализация переменных и установка
        spinnerLanguage = (Spinner)  getView().findViewById(R.id.spinner_language);
        spinnerTheme = (Spinner)  getView().findViewById(R.id.spinner_theme);
        int Theme = ((MainActivity)getActivity()).getCurrentTheme();
        int Language = ((MainActivity)getActivity()).getCurrentLanguage();
        spinnerTheme.setSelection(Theme,false);
        spinnerLanguage.setSelection(Language,false);
        // 2 Обработчика событий при нажатии на Spinner-ы
        initSpinnerLanguage(); initSpinnerTheme();
    }

    public void initSpinnerLanguage(){
        spinnerLanguage.getBackground().setColorFilter(getResources().getColor(R.color.textColorDark), PorterDuff.Mode.SRC_ATOP);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).setCurrentLanguage(position);
                ((MainActivity)getActivity()).doPreferences(true);
                getActivity().recreate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void initSpinnerTheme(){
        spinnerTheme.getBackground().setColorFilter(getResources().getColor(R.color.textColorDark), PorterDuff.Mode.SRC_ATOP);
        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).setCurrentTheme(position);
                ((MainActivity)getActivity()).doPreferences(true);
                getActivity().recreate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
