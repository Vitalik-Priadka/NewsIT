package com.priadka.newsit_project.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.priadka.newsit_project.Constant;

public class LoadFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstaneState) {
        return inflater.inflate(Constant.LOAD, container, false);
    }
}