package com.priadka.newsit_project;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static  final int LAYOUT =R.layout.activity_main;
    private  Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        initNavigationView();
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }
    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }
}
