package com.priadka.newsit_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private EditText editText;
    /*TODO Task
        REST API using retrofit
        Активити для статьи
        Меню NavigationView
        Реализовать добавление news_layout и затычку
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(Constant.LAYOUT);
        initToolbar();
        initNavigationView();
        KeyboardAction();
    }

    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editText = (EditText) findViewById(R.id.search_text);
        toolbar.setTitle(null);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.search:{
                        hideKeyboard();
                        Search(editText);
                        break;
                    }
                    case R.id.reload:{
                        hideKeyboard();
                        ReloadPage();
                        break;
                    }
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) hideKeyboard();
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }

    public void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout , toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers(); hideKeyboard();
                switch (menuItem.getItemId()){
                    case R.id.actionLogInItem:{
                        GoToPage(LoginActivity.class);
                        break;
                    }
                    case R.id.actionNewsItem:{
                        GoToPage(MainActivity.class);
                        break;
                    }
                    case R.id.actionSettingItem:{
                        GoToPage(SettingActivity.class);
                        break;
                    }
                    case R.id.actionExitItem: {
                        finishAndRemoveTask (); break;}
                }
                return true;
            }
        });
    }

    public void ReloadPage(){
        /* ImageView drawable.icon =(ImageView) findViewById(R.id.reload);;
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        drawable.icon.startAnimation(rotate); */
        Toast.makeText(MainActivity.this, R.string.reload_toast, Toast.LENGTH_SHORT).show();
    }
    public  void Search(EditText text){
        if(text != null) {
            Toast.makeText(MainActivity.this, R.string.search_toast, Toast.LENGTH_SHORT).show();
            text.setText(null);
        }
    }

    private void GoToPage(Class activity) {
        if (activity != getClass()) {
            Intent intent = new Intent(this, activity);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        return;
    }

    public void ShortToolbar(int captionText){
        findViewById(R.id.search_text).setVisibility(View.INVISIBLE);
        findViewById(R.id.search).setVisibility(View.INVISIBLE);
        findViewById(R.id.reload).setVisibility(View.INVISIBLE);
        TextView caption = (TextView) findViewById(R.id.caption_page);
        caption.setText(captionText);
        caption.setVisibility(View.VISIBLE);
    }
    private  void KeyboardAction() {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    Search(editText);
                    return true;
                }
                return false;
            }
        });
    }
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
