package com.priadka.newsit_project;

import android.os.Bundle;

public class SettingActivity extends MainActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Constant.SETTING);
        initToolbar();
        initNavigationView();
        ShortToolbar(R.string.menu_item_settings);
    }
}
