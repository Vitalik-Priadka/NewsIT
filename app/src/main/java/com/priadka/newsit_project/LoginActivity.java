package com.priadka.newsit_project;

import android.os.Bundle;

public class LoginActivity extends MainActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Constant.LOG);
        initToolbar();
        initNavigationView();
        ShortToolbar(R.string.menu_item_log_in);
    }
}