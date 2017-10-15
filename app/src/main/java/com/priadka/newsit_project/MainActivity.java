package com.priadka.newsit_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.priadka.newsit_project.fragment.LoginFragment;
import com.priadka.newsit_project.fragment.NewsFragment;
import com.priadka.newsit_project.fragment.SettingFragment;

import java.lang.reflect.Field;

public class MainActivity extends FragmentActivity {

    private Toolbar toolbar;    private DrawerLayout drawerLayout;  private EditText searchField;
    private RelativeLayout navigationHeader;
    private LoginFragment loginFragment;  private  SettingFragment settingFragment; private NewsFragment newsFragment;
    private FragmentManager manager;    private FragmentTransaction transaction;
    public boolean isLogin = false; private int currentAvatar = 9; private static long back_pressed;
    /*TODO Task
        REST API using retrofit
        Активити для статьи
        Реализовать добавление state_layout и затычку
        Логин пользователя
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(Constant.LAYOUT);

        initToolbar();
        initNavigationView();
    }
    @Override
    protected void onStart() {
        super.onStart();
        KeyboardAction();
        reloadPage();
        manager = getSupportFragmentManager();
        loginFragment = new LoginFragment();
        settingFragment = new SettingFragment();
        newsFragment = new NewsFragment();
        FragmentDo(newsFragment);
    }

    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchField = (EditText) findViewById(R.id.search_text);
        toolbar.setTitle(null);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.search:{
                        hideKeyboard();
                        search(searchField.getText().toString());
                        break;
                    }
                    case R.id.reload:{
                        hideKeyboard();
                        reloadPage();
                        break;
                    }
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }
    public void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationHeader = (RelativeLayout) findViewById(R.id.navigation_header);
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
                        FragmentDo(loginFragment);
                        break;
                    }
                    case R.id.actionLogOutItem:{
                        isLogin = false;
                        initNavigationOnLogin();
                        reloadPage();
                        break;
                    }
                    case R.id.actionBookmarks:{
                        //FragmentDo(bookmarksFragment);
                        break;
                    }
                    case R.id.actionNewsItem:{
                        FragmentDo(newsFragment);
                        break;
                    }
                    case R.id.actionSettingItem:{
                        FragmentDo(settingFragment);
                        break;
                    }
                    case R.id.actionExitItem: {
                        finishAffinity();
                        break;}
                }
                return true;
            }
        });

    }
    public void initNavigationOnLogin() {
        ImageView avatarImage  = (ImageView) findViewById(R.id.menu_avatar);
        TextView fieldUserName = (TextView) findViewById(R.id.menu_nickname);
        TextView fieldUserMail = (TextView) findViewById(R.id.menu_email);
        navigationHeader = (RelativeLayout) findViewById(R.id.navigation_header);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        if(isLogin){
            MenuItem LogOutButton = navigationView.getMenu().findItem(R.id.actionLogOutItem);   LogOutButton.setVisible(true);
            MenuItem BookmarksButton = navigationView.getMenu().findItem(R.id.actionBookmarks); BookmarksButton.setVisible(true);
            MenuItem LogInButton = navigationView.getMenu().findItem(R.id.actionLogInItem);     LogInButton.setVisible(false);
            this.invalidateOptionsMenu();

            // Чтение профиля и установка
            fieldUserName.setText("Vitalik");
            fieldUserMail.setText("vitalik.pryadka@gmail.com");
            String currentAvatarS = "avatar_" + currentAvatar;
            avatarImage.setImageResource(getResId(currentAvatarS, R.drawable.class));
            navigationHeader.setVisibility(View.VISIBLE);
        }
        else {
            MenuItem LogOutButton = navigationView.getMenu().findItem(R.id.actionLogOutItem);   LogOutButton.setVisible(false);
            MenuItem BookmarksButton = navigationView.getMenu().findItem(R.id.actionBookmarks); BookmarksButton.setVisible(false);
            MenuItem LogInButton = navigationView.getMenu().findItem(R.id.actionLogInItem);     LogInButton.setVisible(true);
            this.invalidateOptionsMenu();
            navigationHeader.setVisibility(View.INVISIBLE);
        }
    }

    public void reloadPage(){
        /* ImageView drawable.icon =(ImageView) findViewById(R.id.reload);;
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        drawable.icon.startAnimation(rotate); */
        Toast.makeText(MainActivity.this, R.string.reload_toast, Toast.LENGTH_SHORT).show();
    }
    public void search(String request){
        if(request.length() > 0) {
            Toast.makeText(MainActivity.this, R.string.search_toast, Toast.LENGTH_SHORT).show();
            searchField.setText(null);
        }
    }
    public void setImageAvatarNext(View view){
        ImageView avatarImage  = (ImageView) findViewById(R.id.menu_avatar);
        if(currentAvatar < 0 || currentAvatar >= 12){
            currentAvatar = 1;
        }
        else currentAvatar++;
        String currentAvatarS = "avatar_" + currentAvatar;
        avatarImage.setImageResource(getResId(currentAvatarS, R.drawable.class));
    }
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void FragmentDo(Fragment thisFragment){
        if (thisFragment != null && !thisFragment.isVisible()) {
            transaction = manager.beginTransaction();
            transaction.replace(R.id.container, thisFragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }

    public void loginUser(String login, String password){
        if (login.equals("Vitalik") && password.equals("12345")){
            Toast.makeText(MainActivity.this, getString(R.string.log_success)+ " " + login + "!", Toast.LENGTH_SHORT).show();
            try { synchronized(this){wait(200);}
            } catch(InterruptedException ex){ }
            // Получили информацию о пользователе и записали
            isLogin = true;
            initNavigationOnLogin();
            FragmentDo(newsFragment);
        }
        else if(!login.equals("Vitalik")){Toast.makeText(MainActivity.this, getString(R.string.log_error_login), Toast.LENGTH_SHORT).show();}
        else {Toast.makeText(MainActivity.this, getString(R.string.log_error_password), Toast.LENGTH_SHORT).show();}
    }
    public void submitToEmail(View view) {
        // Отправка на email
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: it.news@dev.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.setting_info_email_header));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.setting_info_email_text));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            CharSequence text = getString(R.string.error_text);
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
        return;
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            finishAffinity();
        else
            FragmentDo(newsFragment);
        back_pressed = System.currentTimeMillis();
    }
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private  void KeyboardAction() {
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    search(searchField.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }
}
