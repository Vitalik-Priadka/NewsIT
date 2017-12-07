package com.priadka.newsit_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.priadka.newsit_project.DTO.NewsDTO;
import com.priadka.newsit_project.DTO.UserDTO;
import com.priadka.newsit_project.fragment.HelpFragment;
import com.priadka.newsit_project.fragment.LoadFragment;
import com.priadka.newsit_project.fragment.LoginFragment;
import com.priadka.newsit_project.fragment.NewsFragment;
import com.priadka.newsit_project.fragment.RegisterFragment;
import com.priadka.newsit_project.fragment.SettingFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.widget.Toast.makeText;
import static com.priadka.newsit_project.Constant.APP_PREFERENCES;
import static com.priadka.newsit_project.Constant.DEFAULT_AVATAR;
import static com.priadka.newsit_project.Constant.DEFAULT_THEME;
import static com.priadka.newsit_project.Constant.F_BOOKMARK;
import static com.priadka.newsit_project.Constant.F_EMAIL;
import static com.priadka.newsit_project.Constant.F_IMAGE;
import static com.priadka.newsit_project.Constant.F_LOGIN;
import static com.priadka.newsit_project.Constant.F_STATE;
import static com.priadka.newsit_project.Constant.F_STATE_COMMENTS;
import static com.priadka.newsit_project.Constant.F_S_DATE;
import static com.priadka.newsit_project.Constant.F_S_IMAGE;
import static com.priadka.newsit_project.Constant.F_S_RATING;
import static com.priadka.newsit_project.Constant.F_S_TEXT;
import static com.priadka.newsit_project.Constant.F_S_TITLE;
import static com.priadka.newsit_project.Constant.F_USER;
// Главный класс
public class MainActivity extends FragmentActivity {

    private Toolbar toolbar;                            public static FragmentManager manager;
    private DrawerLayout drawerLayout;                  private NavigationView navigationView;
    private EditText searchField;                       private LoginFragment loginFragment;
    private SharedPreferences mSettings;                private SettingFragment settingFragment;
    private static long back_pressed;                   private NewsFragment newsFragment;
    private UserDTO user;                               private HelpFragment helpFragment;
    private static List<NewsDTO> dataNews;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser userFire;
    private ProgressDialog progressDialog;

    private boolean savePassword, wantLogin, isConnect, isReload = false;
    private String localEmail, localPassword;
    private int currentTheme,currentLanguage, recreateCount, loginCount;

    /*TODO TaskList:
        + Фрагмент для статьи;
        + Динамическая подгрузка фрагментов (статей);
        + Просморт фрагмента статьи;
        + Изменение темы и языка (без статей);
        + Реализовать алгоритм поиска (желательно по частичному совпадению);
        + Реализовать алгоритм обновления (по дате добавления);
        - Комментирование статей;
        + Добавление в "Закладки"
        + Работа с кешом (сохранение темы, языка, логина, пароля);
        + Логин пользователя;
        + Установка соединения с сервером

        Запросы на сервер:
        + Логин: отправка (логин, пароль), жду ответа если все норм получаю данные пользователя
        + Запросы для изменения (автарки позольвателя и ArrayList "закладок")

        + Получение всех новостей (при обновлении newsFragment)
        - Получение данных одной новости по id (для обновления конктретной статьи)
        - Коментарий: Отправка на сервер (id статьи , имя пользователя, текст коммента)
        + Получение всех новостей которые сожержат "ключ" поиска
        + Получение всех новостей которые находяться ArrayList пользователя (массив id)
     */

    @Override   //Данный метод вызывается при создании контента
    protected void onCreate(Bundle savedInstanceState) {
        //Получаем настройки с файла
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        doPreferences(false);
        // Уст. тему и язык
        getTheme(currentTheme);
        getLanguage(currentLanguage);
        super.onCreate(savedInstanceState);
        setContentView(Constant.LAYOUT); // главный LAYOUT
        // Инициализация
        manager = getSupportFragmentManager();
        loginFragment = new LoginFragment();
        settingFragment = new SettingFragment();
        newsFragment = new NewsFragment();
        helpFragment = new HelpFragment();
        progressDialog = new ProgressDialog(this);
        recreateCount = 0; loginCount = 0;
        // Обработчик текущего состояния подключения
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                isConnect = snapshot.getValue(Boolean.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
        // Обработчик текущего состояния пользователя
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                userFire = firebaseAuth.getCurrentUser();
                if (userFire != null) {
                    wantLogin = true;
                } else {
                    wantLogin = false;
                    initNavigationOnLogin();
                }
                showByConnect();
            }};
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Только при первом запуске
        if (recreateCount == 0){
            initToolbar();
            initNavigationView();
            KeyboardAction();
            FragmentDo(new LoadFragment());
            mAuth.addAuthStateListener(mAuthListener);
            if(wantLogin){
                loginUser(localPassword);
            }
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    if (dataNews != null){
                        return;
                    }
                    else reloadPage();
                }}, Constant.SPLASH_DISPLAY_LENGTH);
            recreateCount++;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        // При сворачивании - сохранять настройки
        doPreferences(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // При возобновлении - читать настройки
        doPreferences(false);
    }

    // Установка темы приложения - после чего необходима перезагрузка
    public Resources.Theme getTheme(int currentTheme) {
        Resources.Theme theme = super.getTheme();
            if (currentTheme < 3 && currentTheme >= 0){
                switch (currentTheme){
                    case 0:{
                        theme.applyStyle(Constant.THEME_1, true);break;
                    }
                    case 1:{
                        theme.applyStyle(Constant.THEME_2, true);break;
                    }
                    case 2:{
                        theme.applyStyle(Constant.THEME_3, true);break;
                    }
                }
            }
        return theme;
    }
    // Установка языка приложения - после чего необходима перезагрузка
    public void getLanguage(int currentLanguage){
        String languageToLoad; Locale locale;
        Configuration configuration = new Configuration();
        switch (currentLanguage){
            case 0:
                languageToLoad = "ru";
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                configuration.setLocale(locale);
                getBaseContext().getResources().updateConfiguration(configuration,
                        getBaseContext().getResources().getDisplayMetrics());
                break;
            case 1:
                languageToLoad = "en";
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                configuration.setLocale(locale);
                getBaseContext().getResources().updateConfiguration(configuration,
                        getBaseContext().getResources().getDisplayMetrics());
                break;
        }
    }
    // Управление переменными (чтение или запись в файл)
    public void doPreferences(boolean save){
        SharedPreferences.Editor editor = mSettings.edit();
        if (save) {
            editor.putBoolean("Value_savePassword", savePassword);
            editor.putBoolean("Value_wantLogin", wantLogin);
            editor.putInt("Value_Theme", currentTheme);
            editor.putInt("Value_Language", currentLanguage);
            editor.putString("Value_Login", localEmail);
            editor.putString("Value_Password", localPassword);
            editor.apply();
        }
        else {
            savePassword = mSettings.getBoolean("Value_savePassword", false);
            wantLogin = mSettings.getBoolean("Value_wantLogin", false);
            currentTheme = mSettings.getInt("Value_Theme",DEFAULT_THEME);
            currentLanguage = mSettings.getInt("Value_Language", 0);
            localEmail = mSettings.getString("Value_Login", "");
            localPassword = mSettings.getString("Value_Password", "");
        }
    }
    // Логин пользователя с исп. сохр. email
    public void loginUser(String password){
        if(!localEmail.isEmpty() && !password.isEmpty()){
            // Началась процедура входа - показ диалога
            progressDialog.setMessage(getString(R.string.log_waiting));
            progressDialog.setCancelable(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(localEmail, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        // Если успешно вошли - получение данных
                        getUserData();
                    }
                    else{
                        // Попытка определить причину неудачи
                        if (progressDialog.isShowing())progressDialog.dismiss();
                        String stateOfWrong = getString(R.string.log_error_connection);
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthException e) {
                            switch (e.getErrorCode()){
                                case "ERROR_INVALID_EMAIL":stateOfWrong = getString(R.string.reg_error_invalid_email);break;
                                case "ERROR_USER_NOT_FOUND":stateOfWrong = getString(R.string.log_error_login);break;
                                case "ERROR_WRONG_PASSWORD":stateOfWrong = getString(R.string.log_error_password);break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        makeText(MainActivity.this,getString(R.string.log_error) + " " + stateOfWrong, Toast.LENGTH_SHORT).show();
                    }}});
        }
    }
    // Получение данных пользователя - установка обработчика изменений данных
    private void getUserData(){
        DatabaseReference myRefUser = FirebaseDatabase.getInstance().getReference().child(F_USER);
        myRefUser.child(userFire.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,String>> data = new GenericTypeIndicator<Map<String,String>>(){};
                Map <String, String> map = dataSnapshot.getValue(data);
                String name = map.get(F_LOGIN);
                String email = map.get(F_EMAIL);
                String imageNumber = map.get(F_IMAGE);
                String bookmark = map.get(F_BOOKMARK);
                ArrayList<String> listString = new ArrayList<>(Arrays.asList(bookmark.split(" ")));
                ArrayList<Integer> listBookmark = getIntegerArray(listString);
                user = new UserDTO(name,email,Integer.valueOf(imageNumber), listBookmark);
                initNavigationOnLogin();
                if (progressDialog.isShowing())progressDialog.dismiss();
                if(loginCount == 0){
                    Toast.makeText(MainActivity.this,getString(R.string.log_success) + " " + user.getUser_login() + "!", Toast.LENGTH_SHORT).show();
                    loginCount++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", databaseError.toString());
            }
        });
    }
    // Получение данных статей - установка обработчика изменений данных
    private void getStateData(){
        dataNews = new ArrayList<>();
        DatabaseReference myRefState = FirebaseDatabase.getInstance().getReference().child(F_STATE);
        Query query = myRefState.orderByKey().limitToLast(20);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot state : dataSnapshot.getChildren()) {
                        getState(state);
                        FragmentDo(newsFragment);
                    }
                    isReload = false;
                    Collections.reverse(dataNews);
                }}
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    // Получение данных статей которые находятся у пользователя в избранном - установка обработчика изменений данных
    private void getStateDataBookmark() {
        dataNews = new ArrayList<>();
        DatabaseReference myRefState = FirebaseDatabase.getInstance().getReference().child(F_STATE);
        Collections.reverse(user.getUser_bookmarksList());
        // Отправка запросов на получение контретной статьи
        for (Object idSearch : user.getUser_bookmarksList()) {
            Query query = myRefState.child(String.valueOf(idSearch)).orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        getState(dataSnapshot);
                    }
                    if (!newsFragment.isVisible())FragmentDo(newsFragment);
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});
        }
        Collections.reverse(user.getUser_bookmarksList());
    }
    // Получение данных статей (всех) и выборка из них только "нужных" - установка обработчика изменений данных
    private void getStateDataSearch(final String request){
        dataNews = new ArrayList<>();
        DatabaseReference myRefState = FirebaseDatabase.getInstance().getReference().child(F_STATE);
        Query query = myRefState.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int itemFound = 0;
                    String requestL = request.toLowerCase(), requestW = request.toUpperCase();
                    for (DataSnapshot state : dataSnapshot.getChildren()) {
                        String titleStringL = state.child(F_S_TITLE).getValue().toString().toLowerCase();
                        String titleStringW = state.child(F_S_TITLE).getValue().toString().toUpperCase();
                        ArrayList<String> titleTextL = new ArrayList<>(Arrays.asList(titleStringL.split(" ")));
                        ArrayList<String> titleTextW = new ArrayList<>(Arrays.asList(titleStringW.split(" ")));
                        if (titleTextL.contains(requestL) || titleTextW.contains(requestW)) {
                            getState(state);
                            itemFound++;
                            FragmentDo(newsFragment);
                        }
                    }
                    if(itemFound == 0){
                        Toast.makeText(MainActivity.this,getString(R.string.search_none), Toast.LENGTH_SHORT).show();
                        reloadPage();
                    }
                }}
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }});
    }
    // Выбор из пакета данных определенных полей (их значений)
    private void getState(DataSnapshot state){
        GenericTypeIndicator<Map<String, Object>> data = new GenericTypeIndicator<Map<String, Object>>() {};
        Map<String, Object> map = state.getValue(data);
        String title = String.valueOf(map.get(F_S_TITLE));
        String text = String.valueOf(map.get(F_S_TEXT));
        String date = String.valueOf(map.get(F_S_DATE));
        String rating = String.valueOf(map.get(F_S_RATING));
        String image = String.valueOf(map.get(F_S_IMAGE));
        String number_child = String.valueOf(state.child(F_STATE_COMMENTS).getChildrenCount());
        String id = state.getKey();

        // Добавление данной статьи в список новостей
        if (title != null && text != null && date != null && rating != null && image != null && number_child != null) {
            dataNews.add(new NewsDTO(Integer.valueOf(id), image, title, text, date, Integer.valueOf(rating), Integer.valueOf(number_child)));
        }
    }

    // Инициализация Toolbar - установка обработчиков нажатий
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
                        break;}
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }
    // Инициализация бокового NavigationView - установка обработчиков нажатий
    public void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout , toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers(); hideKeyboard(); doPreferences(true);
                switch (menuItem.getItemId()){
                    case R.id.actionLogInItem:{
                        FragmentDo(loginFragment);
                        break;
                    }
                    case R.id.actionRegisterItem:{
                        RegisterFragment registerFragment = new RegisterFragment();
                        FragmentDo(registerFragment);
                        break;
                    }
                    case R.id.actionLogOutItem:{
                        mAuth.signOut();
                        loginCount = 0;
                        break;
                    }
                    case R.id.actionBookmarks:{
                        getBookmarks();
                        break;
                    }
                    case R.id.actionNewsItem:{
                        reloadPage();
                        break;
                    }
                    case R.id.actionSettingItem:{
                        FragmentDo(settingFragment);
                        break;
                    }
                    case R.id.actionExitItem: {
                        exitApp();
                        break;}
                }
                return true;
            }
        });
    }
    // Метод изменения вида NavigationView в зависимости от состояния пользователя
    public void initNavigationOnLogin() {
        View hView = navigationView.getHeaderView(0);
        ImageView avatarImage  = (ImageView)hView.findViewById(R.id.menu_avatar);
        TextView fieldUserName = (TextView)hView.findViewById(R.id.menu_nickname);
        TextView fieldUserMail = (TextView)hView.findViewById(R.id.menu_email);
        RelativeLayout headerImage = (RelativeLayout) hView.findViewById(R.id.navigation_header);

        MenuItem LogInButton = navigationView.getMenu().findItem(R.id.actionLogInItem);
        MenuItem LogOutButton = navigationView.getMenu().findItem(R.id.actionLogOutItem);
        MenuItem RegisterButton = navigationView.getMenu().findItem(R.id.actionRegisterItem);
        MenuItem BookmarksButton = navigationView.getMenu().findItem(R.id.actionBookmarks);

        if(userFire != null){
            LogOutButton.setVisible(true);
            RegisterButton.setVisible(false);
            BookmarksButton.setVisible(true);
            LogInButton.setVisible(false);
            this.invalidateOptionsMenu();

            // Чтение профиля и установка
            if (user.getUser_login() != null && user.getUser_email() != null){
                fieldUserName.setText(user.getUser_login());
                fieldUserMail.setText(user.getUser_email());
                String currentAvatarS = "avatar_" + user.getUser_image();
                avatarImage.setImageResource(getResId(currentAvatarS, R.drawable.class));
            }
            headerImage.setVisibility(View.VISIBLE);
        }
        else {
            LogOutButton.setVisible(false);
            RegisterButton.setVisible(true);
            BookmarksButton.setVisible(false);
            LogInButton.setVisible(true);
            this.invalidateOptionsMenu();
            headerImage.setVisibility(View.INVISIBLE);
        }
    }
    // Изменение аватарки пользователя
    public void setImageAvatarNext(View view){
        ImageView avatarImage  = (ImageView) findViewById(R.id.menu_avatar);
        if(user.getUser_image() < 0 || user.getUser_image() >= 12){
            user.setUser_image(DEFAULT_AVATAR);
        }
        else user.setUser_image(user.getUser_image() + 1);
        String currentAvatarS = "avatar_" + user.getUser_image();
        avatarImage.setImageResource(getResId(currentAvatarS, R.drawable.class));
    }
    // Вспомогательная ф-ция поиска id ресурса по имени
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Спец: Запрос на перезагрузку страницы
    public void reloadPage(){
        // Проверка - не перезагружается уже страница?
        if (!isReload){
            isReload = true;
            // Установка анимации иконки
            if (toolbar != null){
                View icon =  toolbar.findViewById(R.id.reload);
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(750);
                rotate.setInterpolator(new LinearInterpolator());
                icon.startAnimation(rotate);
            }
            // В зависимости от подключения начало загрузки или показ сообщения
            if (isConnect){
                LoadFragment loadFragment = new LoadFragment();
                FragmentDo(loadFragment);
                getStateData();
            }
            else{
                FragmentDo(helpFragment);
                isReload = false;
            }
        }
    }
    // Спец: Запрос на получение "закладок"
    public void getBookmarks(){
        if (userFire != null){
            if (user.getUser_bookmarksList().isEmpty() || !isConnect)FragmentDo(helpFragment);
            else{
                // Показываем loadFragment пока не загрузим первую статью
                LoadFragment loadFragment = new LoadFragment();
                FragmentDo(loadFragment);
                getStateDataBookmark();
            }
        }
    }
    // Спец: Запрос на поиск ключа
    public void search(String request){
        if(request.length() > 0) {
            if (isConnect){
                LoadFragment loadFragment = new LoadFragment();
                FragmentDo(loadFragment);
                getStateDataSearch(request);
            }
            searchField.setText(null);
        }
        searchField.clearFocus();
    }

    // Метод переключения фрагментов
    public void FragmentDo(Fragment thisFragment){
        FragmentTransaction transaction;
        if (thisFragment != null && !thisFragment.isVisible()) {
            transaction = manager.beginTransaction();
            transaction.replace(R.id.container, thisFragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
    }
    // Метод отправки интента сторонним приложениям email
    public void submitToEmail(View view) {
        // Отправка на email
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: it.news@dev.com"));
        String userName = "Anonymous";
        if(user.getUser_login() != null && userFire != null) userName = user.getUser_login();
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.setting_info_email_header)+ " " + userName);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.setting_info_email_text));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            CharSequence text = getString(R.string.error_text);
            Toast toast = makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override   // Обработчик события при нажатии на "назад"
    public void onBackPressed() {
        if (back_pressed + 1200 > System.currentTimeMillis()) {
            exitApp();
        }
        else {
            getCurrentFocus().clearFocus();
            if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawers();
            showByConnect();
        }
        back_pressed = System.currentTimeMillis();
    }
    // Вспомогательная ф-ция скрытия клавиатуры
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    // Обработчик события при нажатии на кнопку поиска (клавиатуры)
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
    // Вспомогательная ф-ция преобразования String массива в Integer
    private ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<>();
        for(String stringValue : stringArray) {
            try {
                result.add(Integer.parseInt(stringValue));
            } catch(NumberFormatException nfe) {
                Log.w("NumberFormat", "Parsing failed! " + stringValue + " can not be an integer");}
        }
        return result;
    }
    // Вспомогательная ф-ция ... я не знаю как объяснить..
    private void showByConnect(){
        if (isConnect){
            if (dataNews != null){
                FragmentDo(newsFragment);
            }
            else reloadPage();
        }
        else {
            FragmentDo(helpFragment);
        }
    }
    private void exitApp(){
        try {
            Thread.sleep(400);
            finishAffinity();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Getter and Setter
    public UserDTO getUser() {return user;}
    public List<NewsDTO> getNews(){return dataNews;}

    public int getCurrentTheme(){return currentTheme;}
    public void setCurrentTheme(int value){currentTheme = value;}
    public int getCurrentLanguage() {return currentLanguage;}
    public boolean getIsConnect() {return isConnect;}
    public void setCurrentLanguage(int currentLanguage) {this.currentLanguage = currentLanguage;}

    public String getLocalEmail(){return localEmail;}
    public void setLocalEmail(String value){localEmail = value;}
    public String getLocalPassword(){return localPassword;}
    public void setLocalPassword(String value){localPassword = value;}
    public boolean getSavePassword(){return savePassword;}
    public void setSavePassword(boolean values){savePassword = values;}
}
